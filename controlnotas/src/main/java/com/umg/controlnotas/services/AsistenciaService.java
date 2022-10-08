package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {

    @Transactional
    ResponseDataDto registrarNuevaPlantillaListadoAsistencia(ListadoAsistenciaDto listadoAsistenciaDto);
    List<ListadoAsistenciaDto> consultarListadoAsistencia(Long idSeccion, LocalDate fecha);
}
