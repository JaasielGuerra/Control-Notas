package com.umg.controlnotas.model.query;

import java.time.LocalDate;

public interface AlumnoEditar {

    public Long getId();

    public String getCodigo();

    public String getNombre();

    public String getApellido();

    public String getDireccion();

    public LocalDate getNacimiento();

    public Long getGrado();

    public Long getSeccion();


}
