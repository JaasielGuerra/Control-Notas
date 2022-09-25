package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.DetalleLectura;
import com.umg.controlnotas.model.Libro;
import com.umg.controlnotas.model.dto.*;
import com.umg.controlnotas.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log
public class LecturaServiceImpl implements LecturaService {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private DetalleLecturaRepository detalleLecturaRepository;
    @Autowired
    private BimestreRepository bimestreRepository;
    @Autowired
    private LibroRepository libroRepository;

    /**
     * listar los grados con la cantidad de alumnos que tiene
     *
     * @return List<GradoLecturaDto>
     */
    @Override
    public List<GradoLecturaDto> obtenerGradosLectura() {

        log.info("obtenerGradosLectura");

        List<GradoLecturaDto> grados = gradoRepository.consultarGradosLectura()
                .stream()
                .map(GradoLecturaDto::from)
                .collect(Collectors.toList());

        log.info("grados: " + grados);

        return grados;
    }

    /**
     * listar los alumnos para lectura
     *
     * @param idGrado
     * @return
     */
    @Override
    public List<AlumnoLecturaDto> obtenerAlumnosLectura(long idGrado) {

        List<AlumnoLecturaDto> alumnos = alumnoRepository.listarAlumnosLectura(Alumno.ACTIVO, idGrado)
                .stream()
                .map(AlumnoLecturaDto::from)
                .collect(Collectors.toList());

        log.info("alumnos: " + alumnos);

        return alumnos;
    }

    /**
     * obtener un alumno para registro de lectura
     *
     * @param idAlumno
     * @return
     */
    @Override
    public AlumnoLecturaDto obtenerAlumnoLectura(long idAlumno) {

        log.info("obtenerAlumnoLectura");

        AlumnoLecturaDto alumno = AlumnoLecturaDto.from(alumnoRepository.obtenerAlumnoLectura(idAlumno));
        if (alumno == null) {
            log.info("alumno no encontrado");
            throw new NoSuchElementException("alumno no encontrado");
        }

        return alumno;
    }

    /**
     * obtener el detalle de lectura de un alumno
     *
     * @param idAlumno
     * @param idBimestre
     * @return
     */
    @Override
    public List<DetalleLecturaDto> obtenerDetalleLectura(long idAlumno, long idBimestre) {

        log.info("obtenerDetalleLectura");

        List<DetalleLecturaDto> detalle = detalleLecturaRepository.findByIdAlumnoIdAndIdBimestreIdOrderByIdDesc(idAlumno, idBimestre)
                .stream()
                .map(DetalleLecturaDto::from)
                .collect(Collectors.toList());

        log.info("detalle: " + detalle);

        return detalle;
    }

    /**
     * registrar el detalle de lectura de un alumno
     *
     * @param registroDetalleLecturaDto
     * @param idBimestre
     * @return
     */
    @Override
    @Transactional
    public ResponseDataDto registrarDetalleLectura(RegistroDetalleLecturaDto registroDetalleLecturaDto, long idBimestre) {

        log.info("registrar DetalleLectura");

        //obtener las entidades relacionadas al detalle de lectura
        Alumno alumno = alumnoRepository.getReferenceById(registroDetalleLecturaDto.getIdAlumno());
        Bimestre bimestre = bimestreRepository.getReferenceById(idBimestre);
        Libro libro = libroRepository.getReferenceById(registroDetalleLecturaDto.getIdLibro());

        //llenar la entidad
        DetalleLectura detalleLectura = new DetalleLectura();
        detalleLectura.setFecha(LocalDate.now());
        detalleLectura.setTipoOperacion(registroDetalleLecturaDto.getOperacion());
        detalleLectura.setPaginasLeidas(registroDetalleLecturaDto.getPaginasLeidas() == null ? 0 : registroDetalleLecturaDto.getPaginasLeidas());
        detalleLectura.setIdLibro(libro);
        detalleLectura.setIdAlumno(alumno);
        detalleLectura.setIdBimestre(bimestre);

        detalleLecturaRepository.save(detalleLectura);//guardar el detalle de lectura

        //segun la operacion, poner mensaje de respuesta
        String  operacion = "";
        switch (registroDetalleLecturaDto.getOperacion()){
            case DetalleLectura.OPERACION_ASIGNACION_LIBRO:
                operacion = "ASIGNACIÓN LIBRO";
                break;
            case DetalleLectura.OPERACION_AVANCE_LECTURA:
                operacion = "AVANCE LECTURA";
                break;
            case DetalleLectura.OPERACION_LIBRO_TERMINADO:
                operacion = "TERMINAR LECTURA";
                break;
        }

        return ResponseDataDto.builder()
                .message("Operación <strong>" + operacion + "</strong> se ha realizado correctamente")
                .code(1)
                .data(registroDetalleLecturaDto)
                .build();
    }
}
