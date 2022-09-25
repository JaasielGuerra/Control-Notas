package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Libro;
import com.umg.controlnotas.model.query.ConsultaEditarLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l.id AS id, l.nombre AS nombre, l.descripcion AS descripcion FROM Libro l WHERE l.id = ?1")
    ConsultaEditarLibro obtenerLibroEditar(Long id);


    List<ConsultarLibros> findByEstado(
           Integer estado
    );

    @Modifying
    @Query(value = "UPDATE Libro l SET l.estado = 0 WHERE l.id =?1")
    void eliminarLibroById(long id);

    List<ConsultarLibros> findByEstadoAndDisponibilidad(
            Integer estado, Integer disponibilidad
    );



}