package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Bimestre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BimestreRepository extends JpaRepository<Bimestre, Long> {
    Bimestre findByEstado(int estado);
}