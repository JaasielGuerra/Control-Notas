package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultaReporteActitudinalAlumno {

    String getDescripcion();
    LocalDate getFecha();
    String getMateria();
    Double getPuntosRestados();
    Double getPuntosSumados();
    Double getPuntosActuales();
}
