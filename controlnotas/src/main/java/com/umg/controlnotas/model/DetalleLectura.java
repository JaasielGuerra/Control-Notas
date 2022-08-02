package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "detalle_lectura")
public class DetalleLectura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_lectura", nullable = false)
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "tipo_operacion")
    private Integer tipoOperacion;

    @Column(name = "paginas_leidas")
    private Integer paginasLeidas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro idLibro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_control_lectura", nullable = false)
    private ControlLectura idControlLectura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno idAlumno;

}