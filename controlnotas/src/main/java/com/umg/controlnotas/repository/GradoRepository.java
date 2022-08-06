package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.custom.GradoDescripcionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradoRepository extends JpaRepository<Grado, Long> {

    List<GradoDescripcionId> findByEstado(int e);
}