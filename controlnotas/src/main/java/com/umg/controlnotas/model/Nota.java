package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "nota")
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota", nullable = false)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno idAlumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_seccion", nullable = false)
    private Seccion idSeccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ciclo_escolar", nullable = false)
    private CicloEscolar idCicloEscolar;

    @Column(name = "prime_informe", length = 150)
    private String primeInforme;

    @Column(name = "fecha_primer_informe")
    private LocalDate fechaPrimerInforme;

    @Column(name = "segundo_informe", length = 150)
    private String segundoInforme;

    @Column(name = "fecha_segundo_informe")
    private LocalDate fechaSegundoInforme;

    @Column(name = "tercer_informe", length = 150)
    private String tercerInforme;

    @Column(name = "fecha_tercer_informe")
    private LocalDate fechaTercerInforme;

    @Column(name = "cuarto_informe", length = 150)
    private String cuartoInforme;

    @Column(name = "fecha_cuarto_informe")
    private LocalDate fechaCuartoInforme;

}