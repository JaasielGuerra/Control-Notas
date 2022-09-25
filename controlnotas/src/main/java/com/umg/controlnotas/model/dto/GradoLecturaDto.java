package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.ConsultaGradoLectura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradoLecturaDto implements Serializable {
    private Long idGrado;
    private String gradoDescripcion;
    private Integer countAlumnos;

    public static GradoLecturaDto from(ConsultaGradoLectura consultaGradoLectura) {
        return GradoLecturaDto.builder()
                .idGrado(consultaGradoLectura.getIdGrado())
                .gradoDescripcion(consultaGradoLectura.getGradoDescripcion())
                .countAlumnos(consultaGradoLectura.getCountAlumnos())
                .build();
    }
}
