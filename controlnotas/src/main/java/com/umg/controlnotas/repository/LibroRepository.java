package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}