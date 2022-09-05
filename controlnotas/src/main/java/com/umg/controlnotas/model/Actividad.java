package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "actividad")
public class Actividad {
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad", nullable = false)
    private Long id;

    @Column(name = "descripcion_actividad", nullable = false)
    private String descripcionActividad;

    @Column(name = "valor_actividad", nullable = false)
    private Double valorActividad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia idMateria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_plan_trabajo", nullable = false)
    private PlanTrabajo idPlanTrabajo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @Column(name = "estado", nullable = false)
    private int estado;
}