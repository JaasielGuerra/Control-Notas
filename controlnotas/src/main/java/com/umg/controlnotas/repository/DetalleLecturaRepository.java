package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DetalleLectura;
import com.umg.controlnotas.model.query.ConsultaDetalleLectura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleLecturaRepository extends JpaRepository<DetalleLectura, Long> {

    List<ConsultaDetalleLectura> findByIdAlumnoIdOrderByIdDesc(Long idAlumno);
}