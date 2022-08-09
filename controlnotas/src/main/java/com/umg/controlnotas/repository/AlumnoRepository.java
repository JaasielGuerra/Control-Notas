package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}