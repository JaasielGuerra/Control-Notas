package com.umg.controlnotas.model.custom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ActividadJSON {

    private String descripcionActividad;
    private Double valorActividad;
    private Long idMateria;
    private Integer estado;
    private Long id;
}
