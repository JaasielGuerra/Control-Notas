package com.umg.controlnotas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloEscolarDto implements Serializable {

    private Long id;
    private Integer anio;
    private LocalDate fechaApertura;
    private Integer diasBaseAsistencia;
    private Long idBimestreActual;
    private String descripcionBimestreActual;
}
