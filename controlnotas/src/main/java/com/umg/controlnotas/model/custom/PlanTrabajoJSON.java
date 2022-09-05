package com.umg.controlnotas.model.custom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanTrabajoJSON {

    private String descripcion;
    private Long idGrado;
    private List<ActividadJSON> actividades;
}
