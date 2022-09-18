package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultarEvaluacion {
    Long getId();
    String getDescripcion();
    LocalDate getFecha();
    Double getPonderacion();
    String getIdTipoEvaluacionDescripcion();
    String getIdMateriaDescripcion();
    String getIdMateriaIdGradoDescripcion();
    Integer getEstado();
}
