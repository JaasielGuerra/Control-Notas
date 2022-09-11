package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.GradoSeccion;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AsignacionAlumnoDto implements Serializable {
    private Long idSeccionAlumno;
    private Long idAlumno;
    private List<GradoSeccion> gradoSeccionList;
}
