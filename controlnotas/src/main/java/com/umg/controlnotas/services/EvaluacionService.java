package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EvaluacionService {

    @Transactional
    ResponseDataDto registrarNuevaEvaluacion(EvaluacionDto evaluacionDto);

    @Transactional(readOnly = true)
    EvaluacionDto obtenerEvaluacionEdtitar(Long id);

    @Transactional
    ResponseDataDto actualizarEvaluacion(EvaluacionDto evaluacionDto, Long id);

    List<EvaluacionDto> consultarEvaluaciones(Long idMateria, Integer idTipoEvaluacion);

    @Transactional
    ResponseDataDto eliminarEvaluacion(Long id);

    List<EvaluacionesMateriaDto> obtenerEvaluacionesCalificar(Long idBimestre, Integer estadoEvaluacion, List<Long> idsMaterias);

    EvaluacionDto obtenerEvaluacionCalificar(Long id);

    @Transactional(readOnly = true)
    List<CalificacionAlumnoDto> obtenerPlantillaCalificarEvaluacion(Long idEvaluacion, List<Long> idsSeccionesAsignadasUsuario);

    @Transactional
    ResponseDataDto guardarCalificacionesEvaluacion(EvaluacionDto evaluacion, List<CalificacionAlumnoDto> calificacionesAlumno);
}
