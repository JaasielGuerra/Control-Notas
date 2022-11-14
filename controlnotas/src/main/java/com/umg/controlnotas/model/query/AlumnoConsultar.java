package com.umg.controlnotas.model.query;

public interface AlumnoConsultar {

    long getId();
    String getCodigo();
    String getNombre();
    String getApellido();
    String getObservacionExpediente();
    String getDescripcionGradoSeccion();
    String getEstadoExpediente();
    Double getPorcentajeAsistencia();
    Long getIdGrado();

}
