package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReporteDetalleActitudinalDto {

    private String descripcion;
    LocalDate fecha;
    private String materia;
    private Double puntosRestados;
    private Double puntosSumados;
    private Double puntosActuales;
}
