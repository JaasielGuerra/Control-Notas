package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
    private Long idGradoId;
    private String tipoEvaluacionDescripcion;
    private String materiaDescripcion;
    private String gradoDescripcion;
    private Integer estado;
    private List<CalificacionAlumnoDto> calificacionesAlumnos;
}
