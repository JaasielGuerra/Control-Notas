package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.dto.BimestreDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.ConsultaBimestresCiclo;
import com.umg.controlnotas.model.query.RubricaUltimoBimestre;
import com.umg.controlnotas.repository.BimestreRepository;
import com.umg.controlnotas.repository.CicloEscolarRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BimestreServiceImpl implements BimestreService {

    @Autowired
    private BimestreRepository bimestreRepository;
    @Autowired
    private CicloEscolarRepository cicloEscolarRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ResponseDataDto activarBimestre(Long idBimestre) {
        var bimestre = bimestreRepository.findById(idBimestre).orElseThrow();
        if (bimestre.getEstado() == 1) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("El bimestre ya se encuentra activo")
                    .build();
        }

        //desactivar bimestre activo
        bimestreRepository.updateEstadoByEstado(Bimestre.CERRADO, Bimestre.ACTIVO);

        //activar bimestre seleccionado
        bimestre.setEstado(Bimestre.ACTIVO);
        bimestreRepository.save(bimestre);

        //refrescar el bimestre en la sesion del usuario
        userFacade.refreshBimestre(bimestre);

        return ResponseDataDto.builder()
                .code(ResponseDataDto.SUCCESS)
                .message("Bimestre activado correctamente, se ha cambiado a " + bimestre.getDescripcion())
                .build();
    }

    /**
     * @param bimestreDto bimestre a aperturar
     * @return ResponseDataDto con mensaje de error o exito
     */
    @Override
    @Transactional
    public ResponseDataDto aperturarBimestre(BimestreDto bimestreDto) {

        List<String> errores = new ArrayList<>();


        //validar que no exista un bimestre aperturado para el ciclo actual
        boolean existeBimestre = bimestreRepository.existsByEstado(Bimestre.ACTIVO);
        if (existeBimestre) {
            errores.add("Ya existe un bimestre aperturado y activo");
        }

        //validar que no se haya el máximo de 4 bimestres para el ciclo actual
        int numeroBimestres = bimestreRepository.countByIdCicloEscolarIdAndEstadoNot(userFacade.getCicloActual().getId(), Bimestre.ELIMINADO);
        if (numeroBimestres >= 4) {
            errores.add("No se puede aperturar un bimestre porque ya se alcanzó el máximo de 4 bimestres para el ciclo actual");
        }

        //validar que el punteo base sea igual a la suma de los puntos de rubrica
        double sumatoriaRubrica = bimestreDto.getPuntosActividades()
                + bimestreDto.getPuntosActitudinal()
                + bimestreDto.getPuntosEvaluaciones();
        if (sumatoriaRubrica != bimestreDto.getPuntosBase()) {
            errores.add("Los puntos base deben ser igual a la sumatoria de la rúbrica");
        }


        //validar que no sobre pase 100 puntos el valor del bimestre
        if (sumatoriaRubrica > 100 || bimestreDto.getPuntosBase() > 100) {
            errores.add("El bimestre no debe sobre pasar los 100 puntos");
        }


        //si hay errores retornar el arreglo de errores y el estado de la operacion como 0 (fallida)
        if (errores.size() > 0) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al aperturar bimestre [" + bimestreDto.getDescripcion() + "] lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        var idCicloActual = userFacade.getCicloActual();
        var idUsuario = userFacade.getUserSession().getIdUsuario();

        Bimestre bimestre = new Bimestre();
        bimestre.setDescripcion(bimestreDto.getDescripcion());
        bimestre.setPuntosBase(bimestreDto.getPuntosBase());
        bimestre.setPuntosActividades(bimestreDto.getPuntosActividades());
        bimestre.setPuntosActitudinal(bimestreDto.getPuntosActitudinal());
        bimestre.setPuntosEvaluaciones(bimestreDto.getPuntosEvaluaciones());
        bimestre.setFechaApertura(LocalDate.now());
        bimestre.setEstado(Bimestre.ACTIVO);
        bimestre.setFechaCommit(LocalDate.now());
        bimestre.setHoraCommit(LocalTime.now());
        bimestre.setIdCicloEscolar(idCicloActual);
        bimestre.setIdUsuario(usuarioRepository.getReferenceById(idUsuario));

        bimestreRepository.save(bimestre);

        //refrescar el bimestre en la sesion del usuario
        userFacade.refreshBimestre(bimestre);

        //retornar los datos registrados en el data atributo
        return ResponseDataDto.builder()
                .code(1)
                .data(bimestreDto)
                .message("Bimestre aperturado con éxito!")
                .build();
    }

    @Override
    public BimestreDto obtenerUltimoBimestre() {
        RubricaUltimoBimestre bimestre = bimestreRepository.findTopByOrderByIdDesc();

        //si no existe un bimestre registrado retornar una instancia vacia
        if (bimestre == null) {
            return new BimestreDto();
        }

        return BimestreDto.builder()
                .id(bimestre.getId())
                .puntosBase(bimestre.getPuntosBase())
                .puntosActitudinal(bimestre.getPuntosActitudinal())
                .puntosActividades(bimestre.getPuntosActividades())
                .puntosEvaluaciones(bimestre.getPuntosEvaluaciones())
                .build();
    }

    @Override
    @Transactional
    public ResponseDataDto cerrarBimestre(Long idBimestre) {

        //obtener el bimestre aperturado
        Bimestre bimestre = bimestreRepository.findById(idBimestre).orElseThrow();

        //cerrar el bimestre
        bimestre.setEstado(Bimestre.CERRADO);
        bimestre.setFechaCierre(LocalDate.now());

        bimestreRepository.save(bimestre);

        //refrescar el bimestre en la sesion del usuario
        userFacade.refreshBimestre(null);

        return ResponseDataDto.builder()
                .code(1)
                .message("Bimestre cerrado con éxito!")
                .build();
    }

    /**
     * Obtener todos los bimestres
     *
     * @return List<BimestreDto>
     */
    @Override
    public List<BimestreDto> obtenerTodosBimestres() {

        List<ConsultaBimestresCiclo> bimestres = bimestreRepository.findBimestresByEstadoInOrderByIdDesc(Arrays.asList(Bimestre.CERRADO, Bimestre.ACTIVO));

        return bimestres.stream().map(b -> BimestreDto.builder()
                .id(b.getId())
                .descripcion(b.getDescripcion())
                .puntosActividades(b.getPuntosActividades())
                .puntosActitudinal(b.getPuntosActitudinal())
                .puntosEvaluaciones(b.getPuntosEvaluaciones())
                .fechaApertura(b.getFechaApertura())
                .fechaCierre(b.getFechaCierre())
                .anioCiclo(b.getIdCicloEscolarAnio())
                .estado(b.getEstado())
                .build()).collect(Collectors.toList());
    }

    /**
     * Obtener ids bimestre por ciclo escolar, y que no esten eliminados
     *
     * @param idCicloEscolar
     */
    @Override
    public List<Long> obtenerIdsBimestre(Long idCicloEscolar) {
        return bimestreRepository.findIdBimestreByIdCicloEscolarId(idCicloEscolar, Bimestre.ELIMINADO);
    }

    /**
     * Obtener bimestre por id ciclo escolar
     *
     * @param idCicloEscolar id ciclo escolar
     * @return List<BimestreDto>
     */
    @Override
    public List<BimestreDto> obtenerBimestresPorCiclo(Long idCicloEscolar) {

        List<ConsultaBimestresCiclo> bimestres = bimestreRepository.findBimestresByIdCicloEscolarIdAndEstadoNot(idCicloEscolar, Bimestre.ELIMINADO);

        return bimestres.stream().map(b -> BimestreDto.builder()
                .id(b.getId())
                .descripcion(b.getDescripcion())
                .puntosActividades(b.getPuntosActividades())
                .puntosActitudinal(b.getPuntosActitudinal())
                .puntosEvaluaciones(b.getPuntosEvaluaciones())
                .fechaApertura(b.getFechaApertura())
                .fechaCierre(b.getFechaCierre())
                .anioCiclo(b.getIdCicloEscolarAnio())
                .estado(b.getEstado())
                .build()).collect(Collectors.toList());
    }
}
