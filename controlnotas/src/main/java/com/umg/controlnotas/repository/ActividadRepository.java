package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Actividad;
import com.umg.controlnotas.model.custom.ActividadEditar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<ActividadEditar> findByIdPlanTrabajoIdAndEstado(Long idPlanTrabajo, int estado);

    @Modifying
    @Query("UPDATE Actividad a SET a.estado = ?2, a.valorActividad = ?3 WHERE a.id = ?1")
    void actualizarActividad(Long id, int estado, double valorActividad);
}