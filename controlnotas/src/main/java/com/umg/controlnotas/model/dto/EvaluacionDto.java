package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluacionDto implements Serializable {
    private Long id;
    private String descripcion;
    private LocalDate fecha;
    private Double ponderacion;
    private Integer idTipoEvaluacionId;
    private Long idMateriaId;
    private String tipoEvaluacionDescripcion;
    private String materiaDescripcion;
    private String gradoDescripcion;
}
