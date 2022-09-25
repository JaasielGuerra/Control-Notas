package com.umg.controlnotas.model.query;

public interface ConsultaAlumnosLectura {

    Long getIdAlumno();

    String getNombre();

    String getApellido();

    String getCodigoAlumno();

    Integer getLibrosLeidos();

    String getLibroActual();

    Integer getPaginasLeidas();
    Integer getTieneLibro();
    Long getIdLibro();
}
