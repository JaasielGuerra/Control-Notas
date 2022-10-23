package com.umg.controlnotas.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AsignacionUsuarioDto implements Serializable {
    private Long idMateria;
    private String descripcionMateria;
    private String descripcionGrado;
    //TODO: agregar idSeccion
}
