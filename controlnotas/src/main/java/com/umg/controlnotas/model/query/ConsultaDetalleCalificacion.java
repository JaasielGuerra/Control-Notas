package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultaDetalleCalificacion {

    Long getId();
    Double getPuntosObtenidos();
    Long getIdActividadId();
    Long getIdAlumnoId();
    LocalDate getFechaCalificacion();
    String getIdAlumnoIdSeccionIdGradoDescripcion();
    String getIdAlumnoIdSeccionDescripcion();
    String getIdAlumnoNombre();
    String getIdAlumnoApellido();

}
