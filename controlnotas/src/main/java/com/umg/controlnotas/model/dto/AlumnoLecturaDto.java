package com.umg.controlnotas.model.dto;

import com.umg.controlnotas.model.query.ConsultaAlumnosLectura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoLecturaDto implements Serializable {

    private Long idAlumno;
    private String nombre;
    private String apellido;
    private String codigoAlumno;
    private Integer librosLeidos;
    private String libroActual;
    private Integer paginasLeidas;
    private Integer tieneLibro;
    private Long idLibro;

    public static AlumnoLecturaDto from (ConsultaAlumnosLectura consultaAlumnosLectura) {
        return AlumnoLecturaDto.builder()
                .idAlumno(consultaAlumnosLectura.getIdAlumno())
                .nombre(consultaAlumnosLectura.getNombre())
                .apellido(consultaAlumnosLectura.getApellido())
                .codigoAlumno(consultaAlumnosLectura.getCodigoAlumno())
                .librosLeidos(consultaAlumnosLectura.getLibrosLeidos())
                .libroActual(consultaAlumnosLectura.getLibroActual())
                .paginasLeidas(consultaAlumnosLectura.getPaginasLeidas())
                .tieneLibro(consultaAlumnosLectura.getTieneLibro())
                .idLibro(consultaAlumnosLectura.getIdLibro())
                .build();
    }
}
