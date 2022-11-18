package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.BimestreDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BimestreService {

    @Transactional
    ResponseDataDto activarBimestre(Long idBimestre);

    @Transactional
    ResponseDataDto aperturarBimestre(BimestreDto bimestreDto);

    BimestreDto obtenerUltimoBimestre();

    @Transactional
    ResponseDataDto cerrarBimestre(Long idBimestre);
    List<BimestreDto> obtenerTodosBimestres();

    List<Long> obtenerIdsBimestre(Long idCicloEscolar);
    List<BimestreDto> obtenerBimestresPorCiclo(Long idCicloEscolar);
}
