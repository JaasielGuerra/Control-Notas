package com.umg.controlnotas.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "detalle_nota")
public class DetalleNota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_nota", nullable = false)
    private Long id;

    @Column(name = "tipo_nota", nullable = false)
    private Integer tipoNota;

    @Column(name = "valor_nota", nullable = false)
    private Double valorNota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia idMateria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bimestre")
    private Bimestre idBimestre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_nota", nullable = false)
    private Nota idNota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

}