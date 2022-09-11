package com.umg.controlnotas.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ActividadDto implements Serializable {

    private String descripcionActividad;
    private Double valorActividad;
    private Long idMateria;
    private Integer estado;
    private Long id;
}
