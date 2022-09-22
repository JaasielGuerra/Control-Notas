package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActividadesMateriaDto implements Serializable {
    private String materia;
    private String gradoMateria;
    List<ActividadDto> actividades;
}
