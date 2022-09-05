package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Materia;
import com.umg.controlnotas.model.custom.MateriaDescripcionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<MateriaDescripcionId> findByIdGradoId(long id);
}