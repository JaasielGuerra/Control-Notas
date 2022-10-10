package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.DetalleListadoDto;
import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {

    @Transactional
    ResponseDataDto registrarNuevaPlantillaListadoAsistencia(ListadoAsistenciaDto listadoAsistenciaDto);
    List<ListadoAsistenciaDto> consultarListadoAsistencia(Long idSeccion, LocalDate fecha);

    ListadoAsistenciaDto consultarListadoAsistenciaPorId(Long id);

    List<DetalleListadoDto> consultarPlantillaListadoAsistencia(Long idListadoAsistencia);

    @Transactional
    ResponseDataDto guardarAsistencia(Long idListado, ListadoAsistenciaDto listadoAsistenciaDto);

    @Transactional
    ResponseDataDto eliminarListadoAsistencia(Long idListado);
}
