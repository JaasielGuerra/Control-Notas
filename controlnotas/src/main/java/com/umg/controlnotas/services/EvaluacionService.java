package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.EvaluacionDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
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
}
