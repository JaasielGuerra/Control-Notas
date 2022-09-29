package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.CicloEscolarDto;

import java.util.List;

public interface CicloEscolarService {

     CicloEscolarDto obtenerCicloActual();

    List<CicloEscolarDto> obtenerCiclosAnteriores();
}
