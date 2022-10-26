package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol {

    public static int ESTADO_ACTIVO = 1;
    public static int ESTADO_INACTIVO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false)
    private Long id;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_commit", nullable = false)
    private LocalDate fechaCommit;

    @Column(name = "hora_commit", nullable = false)
    private LocalTime horaCommit;

}