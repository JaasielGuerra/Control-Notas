package com.umg.controlnotas.services;

import com.umg.controlnotas.model.DetalleListado;
import com.umg.controlnotas.model.ListadoAsistencia;
import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
public class AsistenciaServiceImpl implements AsistenciaService {


    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private ListadoAsistenciaRepository listadoAsistenciaRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private DetalleListadoRepository detalleListadoRepository;

    /**
     * Registrar una nueva plantilla listado de asistencia
     *
     * @param listadoAsistenciaDto Objeto con los datos del listado de asistencia
     * @return Objeto con la respuesta de la operación
     */
    @Override
    @Transactional
    public ResponseDataDto registrarNuevaPlantillaListadoAsistencia(ListadoAsistenciaDto listadoAsistenciaDto) {

        List<Long> alumnos = alumnoRepository.findIdAlumnosActivosBySeccion(listadoAsistenciaDto.getIdSeccion());

        // validar que existan alumnos
        if (Objects.isNull(alumnos) || alumnos.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No existen alumnos activos en la sección seleccionada")
                    .build();
        }

        ListadoAsistencia listado = new ListadoAsistencia();
        listado.setObservacion(listadoAsistenciaDto.getObservacion());
        listado.setFecha(listadoAsistenciaDto.getFecha());
        listado.setIdSeccion(seccionRepository.getReferenceById(listadoAsistenciaDto.getIdSeccion()));
        listado.setEstado(ListadoAsistencia.ESTADO_ACTIVO);
        listado.setFechaCommit(LocalDate.now());
        listado.setHoraCommit(LocalTime.now());
        listado.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
        listado.setIdBimestre(Objects.requireNonNull(userFacade.getBimestreActual(), "El bimestre no puede ser null"));
        listado.setTipo(listadoAsistenciaDto.getTipo());

        listadoAsistenciaRepository.save(listado);


        //recorrer alumnos, y contruis plantilla de tipo detalle_listado
        List<DetalleListado> plantillaListadoAsistencia = alumnos.stream()
                .map(alumno -> {
                    DetalleListado detalle = new DetalleListado();
                    detalle.setIdAlumno(alumnoRepository.getReferenceById(alumno));
                    detalle.setIdListadoAsistencia(listado);
                    detalle.setIdBimestre(Objects.requireNonNull(userFacade.getBimestreActual(), "El bimestre no puede ser null"));
                    return detalle;
                })
                .collect(Collectors.toList());

        detalleListadoRepository.saveAll(plantillaListadoAsistencia);

        //dar el ID recien creado
        listadoAsistenciaDto.setId(listado.getId());

        log.info("Plantilla Listado de asistencia registrado con exito");

        return ResponseDataDto.builder()
                .code(1)
                .data(listadoAsistenciaDto)
                .message("Listado de asistencia registrado correctamente")
                .build();
    }
}
