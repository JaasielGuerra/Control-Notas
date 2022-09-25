package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LibroRepository extends JpaRepository<Libro, Long> {




    List<ConsultarLibros> findByEstado(
           Integer estado
    );

    List<ConsultarLibros> findByEstadoAndDisponibilidad(
            Integer estado, Integer disponibilidad
    );



}