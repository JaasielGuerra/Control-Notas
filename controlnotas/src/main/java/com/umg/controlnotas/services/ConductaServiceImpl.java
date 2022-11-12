package com.umg.controlnotas.services;

import com.umg.controlnotas.model.ControlActitudinal;
import com.umg.controlnotas.model.dto.ControlActitudinalDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.AlumnoConsultar;
import com.umg.controlnotas.model.query.GradoSeccion;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Log
public class ConductaServiceImpl implements ConductaService {

    private final AlumnoRepository alumnoRepository;
    private final SeccionRepository seccionRepository;
    private final UserFacade userFacade;
    private final MateriaRepository materiaRepository;
    private final ControlActitudinalRepository controlActitudinalRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ConductaServiceImpl(
            AlumnoRepository alumnoRepository,
            SeccionRepository seccionRepository,
            UserFacade userFacade,
            MateriaRepository materiaRepository,
            ControlActitudinalRepository controlActitudinalRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.alumnoRepository = alumnoRepository;
        this.seccionRepository = seccionRepository;
        this.userFacade = userFacade;
        this.materiaRepository = materiaRepository;
        this.controlActitudinalRepository = controlActitudinalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Devuelve los grados y secciones a los que tiene acceso el usuario en sesion
     *
     * @return
     */
    @Override
    public List<GradoSeccion> obtenerGradosSeccionesUsuarioSession() {
        //obtener los grado y secciones del usuario para listarlos en la vista
        List<Long> seccionesUsuario = userFacade.obtenerTodosIdsSecciones();
        return seccionRepository.findGradosSeccionesByIdSeccionIn(seccionesUsuario);
    }

    /**
     * Obtener los alumnos para la seccion seleccionada, si se manda null no devuelve nada
     *
     * @param idSeccion
     * @return
     */
    @Override
    public List<AlumnoConsultar> obtenerAlumnosConductaSeccion(Long idSeccion) {

        if (idSeccion == null) {
            return null;
        }

        //obtener los alumnos
        return alumnoRepository.findAlumnosActivosBySeccion(idSeccion);
    }

    /**
     * Registra la calificacion de conducta de un alumno
     *
     * @param controlActitudinal
     * @return
     */
    @Override
    @Transactional
    public ResponseDataDto registrarConducta(ControlActitudinalDto controlActitudinal) {

        // validar que no se envien datos nulos
        if (controlActitudinal == null) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("No se recibieron datos")
                    .build();
        }

        // validar operacion
        if (!Objects.equals(controlActitudinal.getOperacion(), "+") && !Objects.equals(controlActitudinal.getOperacion(), "-")) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("Operación no válida")
                    .build();
        }

        //si no existe registro de conducta para el alumno, cargar los puntos iniciales de conducta
        boolean existeRegistroActitudinal = controlActitudinalRepository.existsByIdMateriaIdAndIdAlumnoIdAndIdBimestreId(
                controlActitudinal.getIdMateria(),
                controlActitudinal.getIdAlumno(),
                userFacade.getBimestreActual().getId()
        );

        // si no existe registro de conducta para el alumno, cargar los puntos iniciales de conducta
        if (!existeRegistroActitudinal) {
            ControlActitudinal puntosIniciales = new ControlActitudinal();
            puntosIniciales.setIdMateria(materiaRepository.getReferenceById(controlActitudinal.getIdMateria()));
            puntosIniciales.setIdAlumno(alumnoRepository.getReferenceById(controlActitudinal.getIdAlumno()));
            puntosIniciales.setDescripcion("PUNTOS INICIALES");
            puntosIniciales.setPuntosRestados(0.0D);
            puntosIniciales.setPuntosSumados(userFacade.getBimestreActual().getPuntosActitudinal());
            puntosIniciales.setPuntosActuales(userFacade.getBimestreActual().getPuntosActitudinal());
            puntosIniciales.setFecha(LocalDate.now());
            puntosIniciales.setFechaCommit(LocalDate.now());
            puntosIniciales.setHoraCommit(LocalTime.now());
            puntosIniciales.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
            puntosIniciales.setIdBimestre(userFacade.getBimestreActual());

            controlActitudinalRepository.save(puntosIniciales);
        }

        //obtener los puntos actuales de conducta del alumno
        double puntosActualesConducta = controlActitudinalRepository.obtenerPuntosConducta(
                userFacade.getBimestreActual().getId(),
                controlActitudinal.getIdAlumno(),
                controlActitudinal.getIdMateria()
        );

        // validar que la suma de puntos no sea mayor a los puntos actitudinales del bimestre
        if (Objects.equals(controlActitudinal.getOperacion(), "+")) {
            double sumaNuevosPuntosActualesConducta = puntosActualesConducta + controlActitudinal.getPuntos();

            if (sumaNuevosPuntosActualesConducta > userFacade.getBimestreActual().getPuntosActitudinal()) {
                return ResponseDataDto.builder()
                        .code(ResponseDataDto.ERROR)
                        .message("El alumno no puede tener más de " + userFacade.getBimestreActual().getPuntosActitudinal() + " puntos de conducta.")
                        .errors(Arrays.asList("Puntos actuales: " + puntosActualesConducta, "Puntos a sumar: " + controlActitudinal.getPuntos(), "Resultado operación: " + sumaNuevosPuntosActualesConducta))
                        .build();
            }
        }

        // validar que la resta de puntos no sea menor a 0
        if (Objects.equals(controlActitudinal.getOperacion(), "-")) {
            double sumaNuevosPuntosActualesConducta = puntosActualesConducta - controlActitudinal.getPuntos();

            if (sumaNuevosPuntosActualesConducta < 0) {
                return ResponseDataDto.builder()
                        .code(ResponseDataDto.ERROR)
                        .message("El alumno no puede tener menos de 0 puntos de conducta.")
                        .errors(Arrays.asList("Puntos actuales: " + puntosActualesConducta, "Puntos a restar: " + controlActitudinal.getPuntos(), "Resultado operación: " + sumaNuevosPuntosActualesConducta))
                        .build();
            }
        }


        ControlActitudinal actitudinal = new ControlActitudinal();
        actitudinal.setIdMateria(materiaRepository.getReferenceById(controlActitudinal.getIdMateria()));
        actitudinal.setIdAlumno(alumnoRepository.getReferenceById(controlActitudinal.getIdAlumno()));
        actitudinal.setDescripcion(controlActitudinal.getDescripcion());
        actitudinal.setFecha(controlActitudinal.getFecha());
        actitudinal.setFechaCommit(LocalDate.now());
        actitudinal.setHoraCommit(LocalTime.now());
        actitudinal.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
        actitudinal.setIdBimestre(userFacade.getBimestreActual());

        //sumar puntos de conducta
        if (Objects.equals(controlActitudinal.getOperacion(), "+")) {
            actitudinal.setPuntosRestados(0.0D);
            actitudinal.setPuntosSumados(controlActitudinal.getPuntos());
            actitudinal.setPuntosActuales(puntosActualesConducta + controlActitudinal.getPuntos());
        }

        // restar puntos de conducta
        if (Objects.equals(controlActitudinal.getOperacion(), "-")) {
            actitudinal.setPuntosRestados(controlActitudinal.getPuntos());
            actitudinal.setPuntosSumados(0.0D);
            actitudinal.setPuntosActuales(puntosActualesConducta - controlActitudinal.getPuntos());
        }

        controlActitudinalRepository.save(actitudinal);

        return ResponseDataDto.builder()
                .code(ResponseDataDto.SUCCESS)
                .message("Calificación de conducta registrada correctamente")
                .build();
    }
}
