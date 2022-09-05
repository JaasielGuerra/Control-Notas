package com.umg.controlnotas.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlanTrabajoRepositoryTest {

    @Autowired
    private PlanTrabajoRepository   planTrabajoRepository;

    @Test
    void findByEstado() {

        assertNotNull(planTrabajoRepository.findByEstado(1));
    }
}