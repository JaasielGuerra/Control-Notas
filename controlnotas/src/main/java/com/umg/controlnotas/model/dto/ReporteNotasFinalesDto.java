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
public class ReporteNotasFinalesDto implements Serializable {

    private String nombreAlumno;
    private String apellidoAlumno;
    private String codigoAlumno;
    private String direccionAlumno;
    private String gradoSeccionAlumno;
    private Integer ciclo;
    private List<DetalleNotaFinalDto> detallesNotaFinal;

}
