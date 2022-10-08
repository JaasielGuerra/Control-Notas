package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.ListadoAsistencia;
import com.umg.controlnotas.model.query.ConsultaListadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ListadoAsistenciaRepository extends JpaRepository<ListadoAsistencia, Long> {

    List<ConsultaListadoAsistencia> findByIdBimestreIdAndEstadoAndIdSeccionIdAndFechaOrderByIdDesc(Long idBimestre, Integer estado, Long idSeccion, LocalDate fecha);
    List<ConsultaListadoAsistencia> findByIdBimestreIdAndEstadoAndFechaOrderByIdDesc(Long idBimestre, Integer estado, LocalDate fecha);

}