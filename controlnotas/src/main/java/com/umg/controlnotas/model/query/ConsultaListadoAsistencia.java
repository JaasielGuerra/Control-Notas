package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultaListadoAsistencia {

    Long getId();
    String getObservacion();
    LocalDate getFecha();
    String getIdSeccionDescripcion();
    String getIdSeccionIdGradoDescripcion();
    Integer getEstado();
    Integer getTipo();

}
