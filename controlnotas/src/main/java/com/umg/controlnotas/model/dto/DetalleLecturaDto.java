package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.ConsultaDetalleLectura;
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
public class DetalleLecturaDto implements Serializable {

    private Long id;
    private LocalDate fecha;
    private Integer operacion;
    private Integer paginasLeidas;
    private String libro;

    public static DetalleLecturaDto from(ConsultaDetalleLectura consultaDetalleLectura) {
        return DetalleLecturaDto.builder()
                .id(consultaDetalleLectura.getId())
                .fecha(consultaDetalleLectura.getFecha())
                .operacion(consultaDetalleLectura.getTipoOperacion())
                .paginasLeidas(consultaDetalleLectura.getPaginasLeidas())
                .libro(consultaDetalleLectura.getIdLibroNombre())
                .build();
    }
}
