package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.AsignacionMateria;
import com.umg.controlnotas.model.query.AsignacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionMateriaRepository extends JpaRepository<AsignacionMateria, Long> {

    List<AsignacionUsuario> findByIdUsuarioIdAndEstado(Long idUsuario, int estado);
}