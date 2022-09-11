package com.umg.controlnotas.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanTrabajoDto implements Serializable {

    private String descripcion;
    private Long idGrado;
    private Long id;
    private List<ActividadDto> actividades;
}
