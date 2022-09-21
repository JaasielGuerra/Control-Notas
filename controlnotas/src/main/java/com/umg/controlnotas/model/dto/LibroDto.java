package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDto implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;



}
