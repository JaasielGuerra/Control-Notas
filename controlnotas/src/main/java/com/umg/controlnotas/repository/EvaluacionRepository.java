package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Evaluacion;
import com.umg.controlnotas.model.query.ConsultarEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.List;

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
}