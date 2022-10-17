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
public class SubDetalleNotaBimestreDto implements Serializable {

    private String materia;
    private String bimestre;
    private Double puntosActividades;
    private Double puntosEvaluaciones;
    private Double puntosConducta;
    private Double puntosTotales;


}
