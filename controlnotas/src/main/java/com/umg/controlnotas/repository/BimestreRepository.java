package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.query.RubricaUltimoBimestre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BimestreRepository extends JpaRepository<Bimestre, Long> {
    Bimestre findByEstado(int estado);
    RubricaUltimoBimestre findTopByOrderByIdDesc();
    boolean existsByEstado(int estado);
}