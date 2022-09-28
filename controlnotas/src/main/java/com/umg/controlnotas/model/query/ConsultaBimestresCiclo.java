package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultaBimestresCiclo {
    Long getId();
    String getDescripcion();
    Double getPuntosActividades();
    Double getPuntosActitudinal();
    Double getPuntosEvaluaciones();
    LocalDate getFechaApertura();
    LocalDate getFechaCierre();
    Integer getIdCicloEscolarAnio();
    Integer getEstado();
}
