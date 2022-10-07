package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "listado_asistencia")
public class ListadoAsistencia {

    public static final int TIPO_ASISTENCIA = 1;
    public static final int TIPO_OTRO = 2;
    public static final int ESTADO_ACTIVO = 1;
    public static final int ESTADO_ELIMINADO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_listado_asistencia", nullable = false)
    private Long id;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_seccion", nullable = false)
    private Seccion idSeccion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_commit", nullable = false)
    private LocalDate fechaCommit;

    @Column(name = "hora_commit", nullable = false)
    private LocalTime horaCommit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_bimestre", nullable = false)
    private Bimestre idBimestre;
    @Column(name="tipo", nullable = false)
    private Integer tipo;

}