package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleNotaFinalDto implements Serializable {

    private String materia;
    private Double bimestre1;
    private Double bimestre2;
    private Double bimestre3;
    private Double bimestre4;
    private Double promedio;
}
