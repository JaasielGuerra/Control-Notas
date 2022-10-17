package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.PlanTrabajo;
import com.umg.controlnotas.model.query.PlanTrabajoConsultar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanTrabajoRepository extends JpaRepository<PlanTrabajo, Long> {

    //funcion existe plan de trabajo por bimestre, grado y estado
    @Query(value = "SELECT func_existe_plan_trabajo(?1,?2)", nativeQuery = true)
    int existsByIdBimestreIdAndIdGradoIdAndEstado(long bimestre, long id_grado);

    List<PlanTrabajoConsultar> findByEstado(int estado);

    List<PlanTrabajoConsultar> findByEstadoAndIdBimestreId(int estado, long idBimestre);
    @Modifying
    @Query(value = "UPDATE PlanTrabajo p SET p.estado = 0 WHERE p.id = ?1")
    void eliminarPlanTrabajo(long id);

    @Query("SELECT p.idGrado.id FROM PlanTrabajo p WHERE p.id = ?1")
    long findIdGgradoIdById(long id);

}