package com.umg.controlnotas.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlumnoServiceImplTest {

    @Autowired
    private AlumnoService alumnoService;

    @Test
    void obtenerDatosExpedienteAlumno() {
        assertNotNull(alumnoService.obtenerDatosExpedienteAlumno(1L));
    }
}