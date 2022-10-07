package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.umg.controlnotas.model.ListadoAsistencia} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListadoAsistenciaDto implements Serializable {
    private Long id;
    private String observacion;
    private LocalDate fecha;
    private Long idSeccion;
    private String gradoSeccion;
    private Integer tipo;
}