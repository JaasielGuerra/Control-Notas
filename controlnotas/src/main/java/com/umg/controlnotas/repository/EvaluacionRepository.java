package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Evaluacion;
import com.umg.controlnotas.model.query.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    @Query(value = "SELECT func_puntos_disponibles_evaluaciones(?1, ?2, ?3);", nativeQuery = true)
    double puntosDisponiblesEvaluaciones(long idMateria, long idBimestre, Long idEvaluacionExcluir);

    List<ConsultarEvaluacion> findByEstadoAndIdBimestreIdAndIdMateriaIdInOrderById(
            int estado,
            long idBimestre,
            Collection<Long> idMateria
    );

    List<ConsultarEvaluacion> findByEstadoAndIdBimestreIdAndIdMateriaIdOrderById(
            int estado,
            long idBimestre,
            long idMateria
    );

    List<ConsultarEvaluacion> findByEstadoAndIdBimestreIdAndIdTipoEvaluacionIdAndIdMateriaIdInOrderById(
            int estado,
            long idBimestre,
            int idTipoEvaluacion,
            Collection<Long> idMateria
    );

    List<ConsultarEvaluacion> findByEstadoAndIdBimestreIdAndIdTipoEvaluacionIdAndIdMateriaIdOrderById(
            int estado,
            long idBimestre,
            int idTipoEvaluacion,
            long idMateria
    );

    @Modifying
    @Query(value = "UPDATE Evaluacion e SET e.estado = 0 WHERE e.id = ?1")
    void eliminarEvaluacionById(long id);

    @Query(value = "CALL proc_consulta_calificar_evaluaciones(:idBimestre,:estadoActividad,:idsMateria)", nativeQuery = true)
    List<ConsultaCalificarEvaluacion> consultaCalificarEvaluaciones(
            @Param("idBimestre") Long idBimestre,
            @Param("estadoActividad") Integer estadoActividad,
            @Param("idsMateria") String idsMateria
    );

    @Query("SELECT a.descripcion AS descripcion, " +
            " a.ponderacion AS ponderacion, a.id AS id, " +
            "m.descripcion AS descripcionMateria," +
            "m.idGrado.id AS idGrado, " +
            "g.descripcion AS descripcionGrado " +
            "FROM Evaluacion a " +
            "JOIN Materia m ON m.id = a.idMateria.id " +
            "JOIN Grado g ON g.id = m.idGrado.id " +
            "WHERE a.id = ?1")
    Optional<ConsultaEvaluacionCalificar> obtenerEvaluacionCalificarById(Long id);
}