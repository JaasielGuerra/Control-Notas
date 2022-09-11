package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DocumentoExpediente;
import com.umg.controlnotas.model.query.DocumentoChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoExpedienteRepository extends JpaRepository<DocumentoExpediente, Long> {

    List<DocumentoChecklist> findByEstado(Integer estado);
}