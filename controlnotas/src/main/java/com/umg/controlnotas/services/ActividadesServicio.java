package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ActividadDto;
import com.umg.controlnotas.model.dto.ActividadesMateriaDto;
import com.umg.controlnotas.model.dto.CalificacionAlumnoDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActividadesServicio {
    List<ActividadesMateriaDto> obtenerActividadesCalificar(Long idBimestre, Integer estadoActividad, List<Long> idsMaterias);
    ActividadDto obtenerActividadCalificar(Long id);
    @Transactional(readOnly = true)
    List<CalificacionAlumnoDto> obtenerPlantillaCalificarActividad(Long idActividad, List<Long> idsSeccionesAsignadasUsuario);

    @Transactional
    ResponseDataDto guardarCalificacionesActividad(ActividadDto actividad, List<CalificacionAlumnoDto> calificacionesAlumno);
}
