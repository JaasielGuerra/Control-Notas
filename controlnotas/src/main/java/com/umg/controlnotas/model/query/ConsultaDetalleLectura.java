package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface ConsultaDetalleLectura {

    LocalDate getFecha();
    Integer getTipoOperacion();
    Integer getPaginasLeidas();
    String getIdLibroNombre();
    Long getId();
}
