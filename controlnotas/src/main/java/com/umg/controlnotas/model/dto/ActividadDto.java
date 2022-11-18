package com.umg.controlnotas.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ActividadDto implements Serializable {

    private String descripcionActividad;
    private Double valorActividad;
    private Long idMateria;
    private Long idGrado;
    private String descripcionGrado;
    private Integer estado;
    private Long id;
    private String descripcionMateria;
    private List<CalificacionAlumnoDto> calificacionesAlumnos;
}
