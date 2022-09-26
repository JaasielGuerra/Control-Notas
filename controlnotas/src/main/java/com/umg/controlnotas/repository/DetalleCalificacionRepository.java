package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DetalleCalificacion;
import com.umg.controlnotas.model.query.ConsultaDetalleCalificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCalificacionRepository extends JpaRepository<DetalleCalificacion, Long> {

    List<ConsultaDetalleCalificacion> findByIdActividadIdAndIdAlumnoIdSeccionIdIn(Long idActividad, List<Long> idsSecciones);
    List<ConsultaDetalleCalificacion> findByIdEvaluacionIdAndIdAlumnoIdSeccionIdIn(Long idActividad, List<Long> idsSecciones);
}