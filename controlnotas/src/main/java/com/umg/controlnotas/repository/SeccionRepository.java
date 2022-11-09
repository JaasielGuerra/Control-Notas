package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.query.SeccionDescripcionId;
import com.umg.controlnotas.model.query.GradoSeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {

    List<SeccionDescripcionId> findByIdGradoId(long id);

    @Query("SELECT " +
            "s.descripcion AS descripcionSeccion, " +
            "g.descripcion AS descripcionGrado, " +
            "s.id AS idSeccion, " +
            "g.id AS idGrado " +
            "FROM " +
            "Seccion s " +
            "JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "g.estado = ?1")
    List<GradoSeccion> findGradosSeccionesByEstadoGrado(int e);
    @Query("SELECT s.idGrado.id FROM Seccion s WHERE s.id = ?1")
    Long obtenerIdGradoPorIdSeccion(Long idSeccion);

    @Query("SELECT " +
            "s.descripcion AS descripcionSeccion, " +
            "g.descripcion AS descripcionGrado, " +
            "s.id AS idSeccion, " +
            "g.id AS idGrado " +
            "FROM " +
            "Seccion s " +
            "JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "s.id IN ?1")
    List<GradoSeccion> findGradosSeccionesByIdSeccionIn(Collection<Long> ids);
}