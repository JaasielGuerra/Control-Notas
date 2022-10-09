package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DetalleListado;
import com.umg.controlnotas.model.query.ConsultaDetalleListado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleListadoRepository extends JpaRepository<DetalleListado, Long> {

    List<ConsultaDetalleListado> findByIdListadoAsistenciaIdOrderByIdAlumnoNombre(Long idListadoAsistencia);
    @Modifying
    @Query("update DetalleListado d set d.temperatura = ?2, d.motivo = ?3, d.observacion = ?4 where d.id = ?1")
    void updateDetalleListado(Long id, Double temperatura, String motivo, String observacion);
}