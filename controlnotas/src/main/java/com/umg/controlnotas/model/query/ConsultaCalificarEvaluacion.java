package com.umg.controlnotas.model.query;

public interface ConsultaCalificarEvaluacion {

    Long getIdEvaluacion();
    String getMateria();
    String getGradoMateria();
    String getDescripcionEvaluacion();
    Double getPonderacionEvaluacion();
    Integer getEstadoEvaluacion();
    Long getIdMateria();
}
