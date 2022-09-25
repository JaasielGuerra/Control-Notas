package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroDetalleLecturaDto implements Serializable {

    private Integer operacion;
    private Integer paginasLeidas;
    private Long idLibro;
    private Long idAlumno;
}
