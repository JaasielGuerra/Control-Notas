package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.InstitucionDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

public interface InstitucionService {

    @Transactional
    ResponseDataDto actualizarDatosInstitucion(InstitucionDto institucionDto, int id);

    InstitucionDto getInstitucion(int id);
}
