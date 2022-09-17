package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Evaluacion;
import com.umg.controlnotas.model.dto.EvaluacionDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.EvaluacionRepository;
import com.umg.controlnotas.repository.MateriaRepository;
import com.umg.controlnotas.repository.TipoEvaluacionRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class EvaluacionServiceImpl implements EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * @param evaluacionDto evaluacionDto a registrar
     * @return ResponseDataDto con el resultado de la operacion
     */
    @Override
    @Transactional
    public ResponseDataDto registrarNuevaEvaluacion(EvaluacionDto evaluacionDto) {

        List<String> errores = new ArrayList<>();

        var bimestre = userFacade.getBimestreActual();

        double puntosDisponibles = evaluacionRepository.puntosDisponiblesEvaluaciones(evaluacionDto.getIdMateriaId(), bimestre.getId(), null);
        if (evaluacionDto.getPonderacion() > puntosDisponibles) {
            errores.add("La evaluación no puede ser creada, debido a que la ponderación es mayor a los puntos disponibles " +
                    "[PUNTOS DISPONIBLES=" + puntosDisponibles + ", PONDERACIÓN=" + evaluacionDto.getPonderacion() + "]");
        }

        if (errores.size() > 0) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al registrar evaluación " + evaluacionDto.getDescripcion() + ", lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        Evaluacion e = new Evaluacion();
        e.setDescripcion(evaluacionDto.getDescripcion().strip());
        e.setFecha(evaluacionDto.getFecha());

        //redondear a 2 decimales
        e.setPonderacion(Math.round(evaluacionDto.getPonderacion() * 100.0) / 100.0);

        e.setIdTipoEvaluacion(tipoEvaluacionRepository.getReferenceById(evaluacionDto.getIdTipoEvaluacionId()));
        e.setIdMateria(materiaRepository.getReferenceById(evaluacionDto.getIdMateriaId()));
        e.setEstado(Evaluacion.ESTADO_ACTIVO);
        e.setFechaCommit(LocalDate.now());
        e.setHoraCommit(LocalTime.now());
        e.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
        e.setIdBimestre(bimestre);

        evaluacionRepository.save(e);

        log.info("Evaluación registrada correctamente: " + e);

        return ResponseDataDto.builder()
                .code(1)
                .data(evaluacionDto)
                .message("Evaluación " + evaluacionDto.getDescripcion() + " registrada correctamente!")
                .build();
    }

    /**
     * @param id de la evaluacion a editar
     * @return EvaluacionDto con los datos de la evaluacion a editar
     */
    @Override
    @Transactional(readOnly = true)
    public EvaluacionDto obtenerEvaluacionEdtitar(Long id) {

        Evaluacion evaluacion = evaluacionRepository.findById(id).orElseThrow();

        log.info("Evaluación a editar: " + evaluacion);

        return EvaluacionDto.builder()
                .id(evaluacion.getId())
                .descripcion(evaluacion.getDescripcion())
                .fecha(evaluacion.getFecha())
                .ponderacion(evaluacion.getPonderacion())
                .idTipoEvaluacionId(evaluacion.getIdTipoEvaluacion().getId())
                .idMateriaId(evaluacion.getIdMateria().getId())
                .build();
    }

    /**
     * @param evaluacionDto evaluacionDto a actualizar
     * @return ResponseDataDto con el resultado de la operacion
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarEvaluacion(EvaluacionDto evaluacionDto, Long id) {

        List<String> errores = new ArrayList<>();

        var bimestre = userFacade.getBimestreActual();

        double puntosDisponibles = evaluacionRepository.puntosDisponiblesEvaluaciones(evaluacionDto.getIdMateriaId(), bimestre.getId(), id);
        if (evaluacionDto.getPonderacion() > puntosDisponibles) {
            errores.add("La evaluación no puede ser creada, debido a que la ponderación es mayor a los puntos disponibles " +
                    "[PUNTOS DISPONIBLES=" + puntosDisponibles + ", PONDERACIÓN=" + evaluacionDto.getPonderacion() + "]");
        }

        if (errores.size() > 0) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al registrar evaluación " + evaluacionDto.getDescripcion() + ", lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        Evaluacion e = evaluacionRepository.findById(id).orElseThrow();
        e.setDescripcion(evaluacionDto.getDescripcion().strip());
        e.setFecha(evaluacionDto.getFecha());
        //redondear a 2 decimales
        e.setPonderacion(Math.round(evaluacionDto.getPonderacion() * 100.0) / 100.0);

        e.setIdTipoEvaluacion(tipoEvaluacionRepository.getReferenceById(evaluacionDto.getIdTipoEvaluacionId()));
        e.setIdMateria(materiaRepository.getReferenceById(evaluacionDto.getIdMateriaId()));

        evaluacionRepository.save(e);

        log.info("Evaluación actualizada correctamente: " + e);

        return ResponseDataDto.builder()
                .code(1)
                .data(evaluacionDto)
                .message("Evaluación " + evaluacionDto.getDescripcion() + " actualizada correctamente!")
                .build();
    }

}
