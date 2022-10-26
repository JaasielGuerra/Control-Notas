package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.dto.CicloEscolarDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.BimestreRepository;
import com.umg.controlnotas.repository.CicloEscolarRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@Log
public class CicloEscolarServiceImpl implements CicloEscolarService {

    @Autowired
    private CicloEscolarRepository cicloEscolarRepository;
    @Autowired
    private BimestreRepository bimestreRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene el ciclo escolar actual
     *
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

        var bimestre = bimestreRepository.findByEstadoAndIdCicloEscolar(Bimestre.ACTIVO, ciclo);

        //refrescar el bimestre en la sesion del usuario
        userFacade.refreshBimestre(bimestre);

        if (bimestre == null) {
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
     *
     * @param id id del ciclo escolar
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto cerrarCiclo(Long id) {

        cicloEscolarRepository.updateEstado(CicloEscolar.CERRADO, id);

        //refrescar el ciclo en la sesion del usuario
        userFacade.refreshCicloEscolar(null);

        return ResponseDataDto.builder()
                .code(1)
                .message("Ciclo escolar cerrado")
                .build();
    }

    @Transactional
    @Override
    public ResponseDataDto aperturarCiclo(CicloEscolarDto cicloEscolarDto) {

        //validar que el año no sea mayor ni menor al actual
        if (cicloEscolarDto.getAnio() < LocalDate.now().getYear() || cicloEscolarDto.getAnio() > LocalDate.now().getYear()) {
            log.warning("El año del ciclo escolar no es valido");
            return ResponseDataDto.builder()
                    .code(0)
                    .message("El año del ciclo escolar debe ser igual al año actual")
                    .build();
        }

        //validar que la fecha de apertura no sea mayor a la fecha actual
        log.info("fecha apertura " + cicloEscolarDto.getFechaApertura() + " , fecha actual " + LocalDate.now() + " , timezone " + TimeZone.getDefault().getDisplayName());
        if (cicloEscolarDto.getFechaApertura().isAfter(LocalDate.now())) {
            log.warning("La fecha de apertura no es valida, fecha apertura " + cicloEscolarDto.getFechaApertura() + " , fecha actual " + LocalDate.now());
            return ResponseDataDto.builder()
                    .code(0)
                    .message("La fecha de apertura no puede ser mayor a la fecha actual")
                    .build();
        }

        //validar que no exista un ciclo escolar aperturado
        var existe = cicloEscolarRepository.existsByEstadoAndAnio(CicloEscolar.APERTURADO, cicloEscolarDto.getAnio());
        if (existe) {
            log.warning("Ya existe un ciclo escolar aperturado para el año " + cicloEscolarDto.getAnio());
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Ya existe un ciclo escolar aperturado para el año " + cicloEscolarDto.getAnio())
                    .build();
        }

        //instalar entidad y llenar atributos
        CicloEscolar cicloEscolar = new CicloEscolar();
        cicloEscolar.setAnio(cicloEscolarDto.getAnio());
        cicloEscolar.setEstado(CicloEscolar.APERTURADO);
        cicloEscolar.setFechaApertura(cicloEscolarDto.getFechaApertura());
        cicloEscolar.setDiasBaseAsistencia(cicloEscolarDto.getDiasBaseAsistencia());
        cicloEscolar.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));

        //guardar en la base de datos
        cicloEscolarRepository.save(cicloEscolar);
        log.info("Ciclo escolar aperturado con exito");

        //refrescar el ciclo en la sesion del usuario
        userFacade.refreshCicloEscolar(cicloEscolar);
        log.info("Ciclo escolar actualizado en la sesion del usuario");

        return ResponseDataDto.builder()
                .code(1)
                .message("Ciclo escolar aperturado")
                .build();
    }
}
