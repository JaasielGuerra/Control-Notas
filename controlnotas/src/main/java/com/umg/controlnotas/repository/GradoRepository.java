package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.query.GradoDescripcionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradoRepository extends JpaRepository<Grado, Long> {

    List<GradoDescripcionId> findByEstado(int e);

    @Query(value="call proc_obtener_grados_sin_plan_trabajo(:id_bimestre)", nativeQuery = true)
    List<GradoDescripcionId> obtenerGradosSinPlanTrabajo(@Param("id_bimestre") long id_bimestre);
}