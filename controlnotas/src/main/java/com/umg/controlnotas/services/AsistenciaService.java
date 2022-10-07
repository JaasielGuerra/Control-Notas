package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

public interface AsistenciaService {

    @Transactional
    ResponseDataDto registrarNuevaPlantillaListadoAsistencia(ListadoAsistenciaDto listadoAsistenciaDto);
}
