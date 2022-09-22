package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Actividad;
import com.umg.controlnotas.model.query.ActividadEditar;
import com.umg.controlnotas.model.query.ConsultaActividadCalificar;
import com.umg.controlnotas.model.query.ConsultaCalificarActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<ActividadEditar> findByIdPlanTrabajoIdAndEstado(Long idPlanTrabajo, int estado);

    @Modifying
    @Query("UPDATE Actividad a SET a.estado = ?2, a.valorActividad = ?3 WHERE a.id = ?1")
    void actualizarActividad(Long id, int estado, double valorActividad);

    @Query(value = "CALL proc_consulta_calificar_actividades(:idBimestre,:estadoActividad,:idsMateria)", nativeQuery = true)
    List<ConsultaCalificarActividad> consultaCalificarActividades(
            @Param("idBimestre") Long idBimestre,
            @Param("estadoActividad") Integer estadoActividad,
            @Param("idsMateria") String idsMateria
    );

    @Query("SELECT a.descripcionActividad AS descripcionActividad, " +
            " a.valorActividad AS valorActividad, a.id AS id, " +
            "a.idMateria.descripcion AS descripcionMateria " +
            "FROM Actividad a WHERE a.id = ?1")
    Optional<ConsultaActividadCalificar> obtenerActividadCalificarById(Long id);
}