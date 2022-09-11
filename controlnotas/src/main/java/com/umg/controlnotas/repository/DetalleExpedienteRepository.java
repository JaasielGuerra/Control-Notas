package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DetalleExpediente;
import com.umg.controlnotas.model.query.DetalleExpedienteEditar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleExpedienteRepository extends JpaRepository<DetalleExpediente, Long> {

    List<DetalleExpedienteEditar> findByIdAlumnoId(Long idAlumno);

    @Modifying
    @Query("UPDATE DetalleExpediente d SET d.estado = ?1 WHERE d.id = ?2")
    void updateEstado(Integer estado, Long id);
}