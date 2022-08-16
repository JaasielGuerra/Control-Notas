package com.umg.controlnotas.model.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AlumnoJSON {

    public AlumnoJSON() {
    }

    private Long id;
    private String codigo;
    private String nombre;
    private String apellido;
    private String direccion;
    private LocalDate nacimiento;
    private Long grado;
    private Long seccion;

}
