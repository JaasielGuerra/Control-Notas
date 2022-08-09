package com.umg.controlnotas.services;

import com.umg.controlnotas.model.custom.AlumnoJSON;
import org.springframework.transaction.annotation.Transactional;

public interface AlumnoService {

    @Transactional
    void registrarAlumno(AlumnoJSON alumno);

}
