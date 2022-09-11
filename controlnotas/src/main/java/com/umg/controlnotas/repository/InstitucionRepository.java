package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Institucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {
}