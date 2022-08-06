package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.custom.SeccionDescripcionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {

    List<SeccionDescripcionId> findByIdGradoId(long id);
}