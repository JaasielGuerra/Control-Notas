package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BimestreDto implements Serializable {
    private Long id;
    private String descripcion;
    private Double puntosBase;
    private Double puntosActividades;
    private Double puntosActitudinal;
    private Double puntosEvaluaciones;
}
