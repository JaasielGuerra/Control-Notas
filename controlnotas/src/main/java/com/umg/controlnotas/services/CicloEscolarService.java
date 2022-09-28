package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.CicloEscolarDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CicloEscolarService {

     CicloEscolarDto obtenerCicloActual();

    List<CicloEscolarDto> obtenerCiclosAnteriores();

    @Transactional
    ResponseDataDto cerrarCiclo(Long id);
}
