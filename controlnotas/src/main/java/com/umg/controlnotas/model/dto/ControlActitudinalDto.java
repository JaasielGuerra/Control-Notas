package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlActitudinalDto implements Serializable {
    private Long id;
    private Long idMateria;
    private Long idAlumno;
    private String descripcion;
    private Double puntosSumados;
    private Double puntosRestados;
    private Double puntosActuales;
    private LocalDate fecha;
    private String operacion;
    private Double puntos;
}
