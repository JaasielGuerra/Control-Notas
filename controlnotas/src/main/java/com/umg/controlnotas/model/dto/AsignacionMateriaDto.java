package com.umg.controlnotas.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AsignacionMateriaDto implements Serializable {
    private Long idMateria;
    private Long idSeccion;
    private String gradoSeccion;
    private String materia;
}
