package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LibroRepository extends JpaRepository<Libro, Long> {




    List<ConsultarLibros> findByEstado(
           Integer estado
    );

@Modifying
    @Query(value = "UPDATE Libro l SET l.estado = 2 WHERE l.id =?1")
    void eliminarLibroById(long id);

}