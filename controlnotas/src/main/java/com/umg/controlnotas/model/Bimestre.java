package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "bimestre")
public class Bimestre {

    public static final int ACTIVO = 1;
    public static final int ELIMINADO = 0;
    public static final int CERRADO = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bimestre", nullable = false)
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Column(name = "puntos_base", nullable = false)
    private Double puntosBase;

    @Column(name = "puntos_actividades", nullable = false)
    private Double puntosActividades;

    @Column(name = "puntos_actitudinal", nullable = false)
    private Double puntosActitudinal;

    @Column(name = "puntos_evaluaciones", nullable = false)
    private Double puntosEvaluaciones;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_commit", nullable = false)
    private LocalDate fechaCommit;

    @Column(name = "hora_commit", nullable = false)
    private LocalTime horaCommit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ciclo_escolar", nullable = false)
    private CicloEscolar idCicloEscolar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

}