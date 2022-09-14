package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.BimestreDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

public interface BimestreService {

    @Transactional
    ResponseDataDto aperturarBimestre(BimestreDto bimestreDto);

    BimestreDto obtenerUltimoBimestre();
}
