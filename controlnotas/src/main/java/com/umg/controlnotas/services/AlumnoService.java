package com.umg.controlnotas.services;

import com.umg.controlnotas.model.custom.*;
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

    @Transactional
    void eliminarAlumno(long idAlumno);


    AsignacionAlumno obtenerAsignacion(long idAlumno);

    @Transactional
    void cambiarAsignacionAlumno(Long idSeccion, long idAlumno);

    List<DocumentoChecklist[]> consultarDocumentosChecklist();

    List<DetalleExpedienteEditar[]> consultarExpedienteAlumno(long idAlumno);

    AlumnoJSON obtenerDatosExpedienteAlumno(long idAlumno);

    @Transactional
    void guardarChecklistExpediente(AlumnoJSON alumno);
}
