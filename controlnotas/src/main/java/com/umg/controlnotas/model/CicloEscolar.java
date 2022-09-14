package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ciclo_escolar")
public class CicloEscolar {

    public static final int APERTURADO = 1;
    public static final int CERRADO = 2;
    public static final int ELIMINADO = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciclo_escolar", nullable = false)
    private Long id;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Column(name = "dias_base_asistencia", nullable = false)
    private Integer diasBaseAsistencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

}