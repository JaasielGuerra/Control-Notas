package com.umg.controlnotas.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InstitucionDto implements Serializable {
    private Integer id;
    private String nombre;
    private String direccion;
    private String codigo;
    private String nombreDirector;
    private String correo;
    private String telefono;
    private String nivel;
    private String sector;
    private String jornada;
}
