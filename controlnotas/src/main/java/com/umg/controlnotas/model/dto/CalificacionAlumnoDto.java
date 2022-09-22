package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalificacionAlumnoDto implements Serializable {

    private Long id;
    private Double puntosObtenidos;
    private Long idActividad;
    private Long idAlumno;
    private Long idEvaluacion;
    private LocalDate fechaCalificacion;
    private String gradoAlumno;
    private String nombreAlumno;

}
