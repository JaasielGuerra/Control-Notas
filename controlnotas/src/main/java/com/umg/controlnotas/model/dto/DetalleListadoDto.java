package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.ConsultaDetalleListado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleListadoDto implements Serializable {

        private Long id;
        private String nombreAlumno;
        private String apellidoAlumno;
        private String codigo;
        private Double temperatura;
        private String motivo;
        private String observacion;

        public static DetalleListadoDto from(ConsultaDetalleListado consultaDetalleListado) {
            return DetalleListadoDto.builder()
                    .id(consultaDetalleListado.getId())
                    .nombreAlumno(consultaDetalleListado.getIdAlumnoNombre())
                    .apellidoAlumno(consultaDetalleListado.getIdAlumnoApellido())
                    .codigo(consultaDetalleListado.getIdAlumnoCodigoAlumno())
                    .temperatura(consultaDetalleListado.getTemperatura())
                    .motivo(consultaDetalleListado.getMotivo())
                    .observacion(consultaDetalleListado.getObservacion())
                    .build();
        }
}
