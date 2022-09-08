package com.umg.controlnotas.model.custom;

public interface ActividadEditar {
    Long getId();
    String getDescripcionActividad();
    Double getValorActividad();
    Long getIdMateriaId();
    String getIdMateriaDescripcion();
    Integer getEstado();
}
