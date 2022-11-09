package com.umg.controlnotas.services;

import com.umg.controlnotas.model.query.AlumnoConsultar;
import com.umg.controlnotas.model.query.GradoSeccion;

import java.util.List;

public interface ConductaService {
    List<GradoSeccion> obtenerGradosSeccionesUsuarioSession();
    List<AlumnoConsultar> obtenerAlumnosConductaSeccion(Long idSeccion);
}
