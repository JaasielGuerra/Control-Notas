package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.PlanTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlanTrabajoRepository extends JpaRepository<PlanTrabajo, Long> {

    //funcion existe plan de trabajo por bimestre, grado y estado
    @Query(value = "SELECT func_existe_plan_trabajo(?1,?2)", nativeQuery = true)
    int existsByIdBimestreIdAndIdGradoIdAndEstado(long bimestre, long id_grado);


}