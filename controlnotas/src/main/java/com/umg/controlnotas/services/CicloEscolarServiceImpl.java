package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.dto.CicloEscolarDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.BimestreRepository;
import com.umg.controlnotas.repository.CicloEscolarRepository;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CicloEscolarServiceImpl implements CicloEscolarService {

    @Autowired
    private CicloEscolarRepository cicloEscolarRepository;
    @Autowired
    private BimestreRepository bimestreRepository;
    @Autowired
    private UserFacade userFacade;

    /**
     * Obtiene el ciclo escolar actual
     * @return CicloEscolarDto
     */
    @Override
    public CicloEscolarDto obtenerCicloActual() {

        var ciclo = cicloEscolarRepository.findByEstadoAndAnio(CicloEscolar.APERTURADO, LocalDate.now().getYear());

        //refrescar el ciclo en la sesion del usuario
        userFacade.refreshCicloEscolar(ciclo);

        if (ciclo == null) {
            return new CicloEscolarDto();
        }

        var bimestre = bimestreRepository.findByEstado(Bimestre.ACTIVO);

        //refrescar el bimestre en la sesion del usuario
        userFacade.refreshBimestre(bimestre);

        if(bimestre == null){
            return CicloEscolarDto.builder()
                    .id(ciclo.getId())
                    .anio(ciclo.getAnio())
                    .diasBaseAsistencia(ciclo.getDiasBaseAsistencia())
                    .fechaApertura(ciclo.getFechaApertura())
                    .idBimestreActual(null)
                    .descripcionBimestreActual("<NO APERTURADO>")
                    .build();
        }

        return CicloEscolarDto.builder()
                .id(ciclo.getId())
                .anio(ciclo.getAnio())
                .diasBaseAsistencia(ciclo.getDiasBaseAsistencia())
                .fechaApertura(ciclo.getFechaApertura())
                .idBimestreActual(bimestre.getId())
                .descripcionBimestreActual(bimestre.getDescripcion())
                .build();
    }

    /**
     * @return
     */
    @Override
    public List<CicloEscolarDto> obtenerCiclosAnteriores() {

        List<CicloEscolar> ciclos = cicloEscolarRepository.findCicloEscolarsByEstadoInOrderByIdDesc(List.of(CicloEscolar.CERRADO, CicloEscolar.APERTURADO));

        return ciclos.stream().map(ciclo -> CicloEscolarDto.builder()
                .id(ciclo.getId())
                .anio(ciclo.getAnio())
                .estado(ciclo.getEstado())
                .fechaApertura(ciclo.getFechaApertura())
                .fechaCierre(ciclo.getFechaCierre())
                .diasBaseAsistencia(ciclo.getDiasBaseAsistencia())
                .build())
                .collect(Collectors.toList());
    }

    /**
     * cerra el ciclo escolar
     * @param id id del ciclo escolar
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto cerrarCiclo(Long id){

        cicloEscolarRepository.updateEstado(CicloEscolar.CERRADO, id);

        //refrescar el ciclo en la sesion del usuario
        userFacade.refreshCicloEscolar(null);

        return ResponseDataDto.builder()
                .code(1)
                .message("Ciclo escolar cerrado")
                .build();
    }
}
