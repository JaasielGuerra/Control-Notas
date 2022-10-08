package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.ConsultaListadoAsistencia;
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
    private Integer estado;

    public static ListadoAsistenciaDto from(ConsultaListadoAsistencia listadoAsistencia) {
        return ListadoAsistenciaDto.builder()
            .id(listadoAsistencia.getId())
            .observacion(listadoAsistencia.getObservacion())
            .fecha(listadoAsistencia.getFecha())
            .gradoSeccion(listadoAsistencia.getIdSeccionIdGradoDescripcion() + " " + listadoAsistencia.getIdSeccionDescripcion())
            .estado(listadoAsistencia.getEstado())
            .build();
    }
}