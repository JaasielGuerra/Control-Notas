package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "institucion")
public class Institucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_institucion", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "codigo", nullable = false, length = 100)
    private String codigo;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_commit", nullable = false)
    private LocalDate fechaCommit;

    @Column(name = "hora_commit", nullable = false)
    private LocalTime horaCommit;

    @Column(name = "nombre_director", nullable = false, length = 150)
    private String nombreDirector;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "telefono", length = 100)
    private String telefono;

    @Column(name = "nivel", length = 100)
    private String nivel;

    @Column(name = "sector", length = 100)
    private String sector;

    @Column(name = "jornada", length = 100)
    private String jornada;

}