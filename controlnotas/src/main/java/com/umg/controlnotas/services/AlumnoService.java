package com.umg.controlnotas.services;

import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.AlumnoEditar;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AlumnoService {

    @Transactional
    void registrarAlumno(AlumnoJSON alumno);

    @Transactional
    void actualizarAlumno(AlumnoJSON alumno);

    Optional<List<AlumnoConsultar>> consultarAlumnos(Long idSeccion);
    public AlumnoEditar obtenerAlumnoEditar(Long id);
}
