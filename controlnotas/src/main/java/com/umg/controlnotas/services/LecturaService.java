package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LecturaService {

    List<GradoLecturaDto> obtenerGradosLectura();

    List<AlumnoLecturaDto> obtenerAlumnosLectura(long idGrado);

    AlumnoLecturaDto obtenerAlumnoLectura(long idAlumno);
    List<DetalleLecturaDto> obtenerDetalleLectura(long idAlumno, long idBimestre);

    @Transactional
    ResponseDataDto registrarDetalleLectura(RegistroDetalleLecturaDto registroDetalleLecturaDto, long idBimestre);
}
