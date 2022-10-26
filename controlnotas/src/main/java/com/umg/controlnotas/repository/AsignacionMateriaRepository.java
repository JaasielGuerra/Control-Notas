package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.AsignacionMateria;
import com.umg.controlnotas.model.query.AsignacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsignacionMateriaRepository extends JpaRepository<AsignacionMateria, Long> {

    List<AsignacionUsuario> findByIdUsuarioIdAndEstado(Long idUsuario, int estado);

    @Modifying
    @Query("UPDATE AsignacionMateria a set a.estado = ?2 where a.idUsuario.id = ?1")
    void updateEstadoAsignacionMateriaByIdUsuario(Long idUsuario, int estado);
}