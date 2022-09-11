package com.umg.controlnotas.services;

import com.umg.controlnotas.model.query.*;
import com.umg.controlnotas.model.dto.AlumnoDto;
import com.umg.controlnotas.model.dto.AsignacionAlumnoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AlumnoService {

    @Transactional
    void registrarAlumno(AlumnoDto alumno);

    @Transactional
    void actualizarAlumno(AlumnoDto alumno);

    Optional<List<AlumnoConsultar>> consultarAlumnos(Long idSeccion);
    public AlumnoEditar obtenerAlumnoEditar(Long id);

    @Transactional
    void eliminarAlumno(long idAlumno);


    AsignacionAlumnoDto obtenerAsignacion(long idAlumno);

    @Transactional
    void cambiarAsignacionAlumno(Long idSeccion, long idAlumno);

    List<DocumentoChecklist[]> consultarDocumentosChecklist();

    List<DetalleExpedienteEditar[]> consultarExpedienteAlumno(long idAlumno);

    AlumnoDto obtenerDatosExpedienteAlumno(long idAlumno);

    @Transactional
    void guardarChecklistExpediente(AlumnoDto alumno);
}
