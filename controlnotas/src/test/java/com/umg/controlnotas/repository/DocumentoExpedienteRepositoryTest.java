package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.DocumentoExpediente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DocumentoExpedienteRepositoryTest {

    @Autowired
    private DocumentoExpedienteRepository documentoExpedienteRepository;

    @Test
    void findByEstado() {

        assertNotNull(documentoExpedienteRepository.findByEstado(DocumentoExpediente.ACTIVO));
    }
}