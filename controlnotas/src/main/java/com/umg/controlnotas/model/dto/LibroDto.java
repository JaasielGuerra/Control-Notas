package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.Libro;
import com.umg.controlnotas.model.query.ConsultaEditarLibro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibroDto implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer disponibilidad;

    public static LibroDto from(ConsultaEditarLibro libro) {
        return LibroDto.builder()
                .id(libro.getId())
                .nombre(libro.getNombre())
                .descripcion(libro.getDescripcion())
                .build();
    }

}
