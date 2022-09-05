package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}