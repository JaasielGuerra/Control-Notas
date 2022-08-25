package com.umg.controlnotas.model.custom;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoJSON {


    private Long id;
    private String codigo;
    private String nombre;
    private String apellido;
    private String direccion;
    private LocalDate nacimiento;
    private Long grado;
    private Long seccion;
    private Integer expediente;
    private String observacion;
    private List<PlantillaChecklist> plantillaChecklists;
}
