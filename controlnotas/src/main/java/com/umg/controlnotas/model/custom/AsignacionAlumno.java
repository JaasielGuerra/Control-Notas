package com.umg.controlnotas.model.custom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AsignacionAlumno {
    private Long idSeccionAlumno;
    private Long idAlumno;
    private List<GradoSeccion> gradoSeccionList;
}
