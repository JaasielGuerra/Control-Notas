package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "control_actitudinal")
public class ControlActitudinal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control_actitudinal", nullable = false)
    private Long id;

    @Column(name = "puntos_iniciales", nullable = false)
    private Double puntosIniciales;

    @Column(name = "puntos_actuales", nullable = false)
    private Double puntosActuales;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia idMateria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno idAlumno;

}