package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.query.RubricaUltimoBimestre;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log
class BimestreRepositoryTest {

    @Autowired
    private BimestreRepository bimestreRepository;


    @Test
    void findTopByOrdOrderByIdDesc() {

        RubricaUltimoBimestre bimestre = bimestreRepository.findTopByOrderByIdDesc();

        assertNotNull(bimestre);


    }
}