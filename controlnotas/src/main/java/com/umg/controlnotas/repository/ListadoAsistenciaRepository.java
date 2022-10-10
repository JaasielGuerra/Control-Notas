package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.ListadoAsistencia;
import com.umg.controlnotas.model.query.ConsultaListadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ListadoAsistenciaRepository extends JpaRepository<ListadoAsistencia, Long> {

    List<ConsultaListadoAsistencia> findByIdBimestreIdAndEstadoAndIdSeccionIdAndFechaOrderByIdDesc(Long idBimestre, Integer estado, Long idSeccion, LocalDate fecha);
    List<ConsultaListadoAsistencia> findByIdBimestreIdAndEstadoAndFechaOrderByIdDesc(Long idBimestre, Integer estado, LocalDate fecha);

    Optional<ConsultaListadoAsistencia> findByEstadoAndId(Integer estado , Long id);
    @Modifying
    @Query("update ListadoAsistencia l set l.observacion = ?2 where l.id = ?1")
    void updateObservacionListadoAsistencia(Long id, String observacion);

    @Modifying
    @Query("update ListadoAsistencia l set l.estado = ?2 where l.id = ?1")
    void updateEstadoListadoAsistencia(Long id, Integer estado);


}