package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    @Query(value="SELECT func_puntos_disponibles_evaluaciones(?1, ?2, ?3);", nativeQuery = true)
    double puntosDisponiblesEvaluaciones(long idMateria, long idBimestre, Long idEvaluacionExcluir);

}