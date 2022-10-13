package com.umg.controlnotas.services;

import com.umg.controlnotas.model.query.AlumnoReporte;

import java.util.List;

public interface ReportesService {
    List<AlumnoReporte> reporteAlumnos(Long seccion);
}
