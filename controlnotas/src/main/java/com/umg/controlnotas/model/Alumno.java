package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "alumno")
public class Alumno {

    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 150)
    private String apellido;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "codigo_alumno", nullable = false, length = 45)
    private String codigoAlumno;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "fecha_commit", nullable = false)
    private LocalDate fechaCommit;

    @Column(name = "hora_commit", nullable = false)
    private LocalTime horaCommit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seccion")
    private Seccion idSeccion;

    @Column(name = "observacion_expediente", length = 150)
    private String observacionExpediente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

}