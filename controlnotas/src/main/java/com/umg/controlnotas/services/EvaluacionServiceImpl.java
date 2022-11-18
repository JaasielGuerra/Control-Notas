package com.umg.controlnotas.services;

import com.umg.controlnotas.model.*;
import com.umg.controlnotas.model.dto.*;
import com.umg.controlnotas.model.query.ConsultaAlumnoCalificacion;
import com.umg.controlnotas.model.query.ConsultaCalificarEvaluacion;
import com.umg.controlnotas.model.query.ConsultaDetalleCalificacion;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
public class EvaluacionServiceImpl implements EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DetalleCalificacionRepository detalleCalificacionRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * @param evaluacionDto evaluacionDto a registrar
     * @return ResponseDataDto con el resultado de la operacion
     */
    @Override
    @Transactional
    public ResponseDataDto registrarNuevaEvaluacion(EvaluacionDto evaluacionDto) {

        List<String> errores = new ArrayList<>();

        var bimestre = userFacade.getBimestreActual();

        double puntosDisponibles = evaluacionRepository.puntosDisponiblesEvaluaciones(evaluacionDto.getIdMateriaId(), bimestre.getId(), null);
        if (evaluacionDto.getPonderacion() > puntosDisponibles) {
            errores.add("La evaluación no puede ser creada, debido a que la ponderación es mayor a los puntos disponibles " +
                    "[PUNTOS DISPONIBLES=" + puntosDisponibles + ", PONDERACIÓN=" + evaluacionDto.getPonderacion() + "]");
        }

        if (errores.size() > 0) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al registrar evaluación " + evaluacionDto.getDescripcion() + ", lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        Evaluacion e = new Evaluacion();
        e.setDescripcion(evaluacionDto.getDescripcion().strip());
        e.setFecha(evaluacionDto.getFecha());

        //redondear a 2 decimales
        e.setPonderacion(Math.round(evaluacionDto.getPonderacion() * 100.0) / 100.0);

        e.setIdTipoEvaluacion(tipoEvaluacionRepository.getReferenceById(evaluacionDto.getIdTipoEvaluacionId()));
        e.setIdMateria(materiaRepository.getReferenceById(evaluacionDto.getIdMateriaId()));
        e.setEstado(Evaluacion.ESTADO_ACTIVO);
        e.setFechaCommit(LocalDate.now());
        e.setHoraCommit(LocalTime.now());
        e.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
        e.setIdBimestre(bimestre);

        evaluacionRepository.save(e);

        log.info("Evaluación registrada correctamente: " + e);

        return ResponseDataDto.builder()
                .code(1)
                .data(evaluacionDto)
                .message("Evaluación " + evaluacionDto.getDescripcion() + " registrada correctamente!")
                .build();
    }

    /**
     * @param id de la evaluacion a editar
     * @return EvaluacionDto con los datos de la evaluacion a editar
     */
    @Override
    @Transactional(readOnly = true)
    public EvaluacionDto obtenerEvaluacionEdtitar(Long id) {

        Evaluacion evaluacion = evaluacionRepository.findById(id).orElseThrow();

        log.info("Evaluación a editar: " + evaluacion);

        return EvaluacionDto.builder()
                .id(evaluacion.getId())
                .descripcion(evaluacion.getDescripcion())
                .fecha(evaluacion.getFecha())
                .ponderacion(evaluacion.getPonderacion())
                .idTipoEvaluacionId(evaluacion.getIdTipoEvaluacion().getId())
                .idMateriaId(evaluacion.getIdMateria().getId())
                .build();
    }

    /**
     * @param evaluacionDto evaluacionDto a actualizar
     * @return ResponseDataDto con el resultado de la operacion
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarEvaluacion(EvaluacionDto evaluacionDto, Long id) {

        List<String> errores = new ArrayList<>();

        var bimestre = userFacade.getBimestreActual();

        double puntosDisponibles = evaluacionRepository.puntosDisponiblesEvaluaciones(evaluacionDto.getIdMateriaId(), bimestre.getId(), id);
        if (evaluacionDto.getPonderacion() > puntosDisponibles) {
            errores.add("La evaluación no puede ser creada, debido a que la ponderación es mayor a los puntos disponibles " +
                    "[PUNTOS DISPONIBLES=" + puntosDisponibles + ", PONDERACIÓN=" + evaluacionDto.getPonderacion() + "]");
        }

        if (errores.size() > 0) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al registrar evaluación " + evaluacionDto.getDescripcion() + ", lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        Evaluacion e = evaluacionRepository.findById(id).orElseThrow();
        e.setDescripcion(evaluacionDto.getDescripcion().strip());
        e.setFecha(evaluacionDto.getFecha());
        //redondear a 2 decimales
        e.setPonderacion(Math.round(evaluacionDto.getPonderacion() * 100.0) / 100.0);

        e.setIdTipoEvaluacion(tipoEvaluacionRepository.getReferenceById(evaluacionDto.getIdTipoEvaluacionId()));
        e.setIdMateria(materiaRepository.getReferenceById(evaluacionDto.getIdMateriaId()));

        evaluacionRepository.save(e);

        log.info("Evaluación actualizada correctamente: " + e);

        return ResponseDataDto.builder()
                .code(1)
                .data(evaluacionDto)
                .message("Evaluación " + evaluacionDto.getDescripcion() + " actualizada correctamente!")
                .build();
    }

    /**
     * Consultar evaluaciones
     *
     * @param idMateria        id de la materia
     * @param idTipoEvaluacion id del tipo de evaluacion
     * @return List<EvaluacionDto> con las evaluaciones encontradas
     */
    @Override
    public List<EvaluacionDto> consultarEvaluaciones(Long idMateria, Integer idTipoEvaluacion) {

        var bimestre = userFacade.getBimestreActual();
        var materiasAsignadasUsuario = userFacade.getAsignacionesUsuario();
        List<Long> idsMaterias = materiasAsignadasUsuario.stream().map(AsignacionUsuarioDto::getIdMateria).collect(Collectors.toList());

        //consultar por materia
        if (Objects.nonNull(idMateria) && Objects.isNull(idTipoEvaluacion)) {

            return evaluacionRepository.findByEstadoAndIdBimestreIdAndIdMateriaIdOrderById(
                            Evaluacion.ESTADO_ACTIVO,
                            bimestre.getId(),
                            idMateria
                    )
                    .stream()
                    .map(evaluacion -> EvaluacionDto.builder()
                            .id(evaluacion.getId())
                            .descripcion(evaluacion.getDescripcion())
                            .fecha(evaluacion.getFecha())
                            .ponderacion(evaluacion.getPonderacion())
                            .tipoEvaluacionDescripcion(evaluacion.getIdTipoEvaluacionDescripcion())
                            .materiaDescripcion(evaluacion.getIdMateriaDescripcion())
                            .gradoDescripcion(evaluacion.getIdMateriaIdGradoDescripcion())
                            .build())
                    .collect(Collectors.toList());
        }

        //consultar por tipo de evaluacion
        if (Objects.nonNull(idTipoEvaluacion) && Objects.isNull(idMateria)) {

            return evaluacionRepository.findByEstadoAndIdBimestreIdAndIdTipoEvaluacionIdAndIdMateriaIdInOrderById(
                            Evaluacion.ESTADO_ACTIVO,
                            bimestre.getId(),
                            idTipoEvaluacion,
                            idsMaterias
                    )
                    .stream()
                    .map(evaluacion -> EvaluacionDto.builder()
                            .id(evaluacion.getId())
                            .descripcion(evaluacion.getDescripcion())
                            .fecha(evaluacion.getFecha())
                            .ponderacion(evaluacion.getPonderacion())
                            .tipoEvaluacionDescripcion(evaluacion.getIdTipoEvaluacionDescripcion())
                            .materiaDescripcion(evaluacion.getIdMateriaDescripcion())
                            .gradoDescripcion(evaluacion.getIdMateriaIdGradoDescripcion())
                            .build())
                    .collect(Collectors.toList());
        }

        //consultar por materia y tipo de evaluacion
        if (Objects.nonNull(idMateria) && Objects.nonNull(idTipoEvaluacion)) {

            return evaluacionRepository.findByEstadoAndIdBimestreIdAndIdTipoEvaluacionIdAndIdMateriaIdOrderById(
                            Evaluacion.ESTADO_ACTIVO,
                            bimestre.getId(),
                            idTipoEvaluacion,
                            idMateria
                    )
                    .stream()
                    .map(evaluacion -> EvaluacionDto.builder()
                            .id(evaluacion.getId())
                            .descripcion(evaluacion.getDescripcion())
                            .fecha(evaluacion.getFecha())
                            .ponderacion(evaluacion.getPonderacion())
                            .tipoEvaluacionDescripcion(evaluacion.getIdTipoEvaluacionDescripcion())
                            .materiaDescripcion(evaluacion.getIdMateriaDescripcion())
                            .gradoDescripcion(evaluacion.getIdMateriaIdGradoDescripcion())
                            .build())
                    .collect(Collectors.toList());
        }

        //consultar todas las evaluaciones
        return evaluacionRepository.findByEstadoAndIdBimestreIdAndIdMateriaIdInOrderById(
                        Evaluacion.ESTADO_ACTIVO,
                        bimestre.getId(),
                        idsMaterias
                )
                .stream()
                .map(evaluacion -> EvaluacionDto.builder()
                        .id(evaluacion.getId())
                        .descripcion(evaluacion.getDescripcion())
                        .fecha(evaluacion.getFecha())
                        .ponderacion(evaluacion.getPonderacion())
                        .tipoEvaluacionDescripcion(evaluacion.getIdTipoEvaluacionDescripcion())
                        .materiaDescripcion(evaluacion.getIdMateriaDescripcion())
                        .gradoDescripcion(evaluacion.getIdMateriaIdGradoDescripcion())
                        .build())
                .collect(Collectors.toList());

    }

    /**
     * @param id id de la evaluacion
     * @return ResponseDataDto con el resultado de la operacion
     */
    @Override
    @Transactional
    public ResponseDataDto eliminarEvaluacion(Long id) {

        evaluacionRepository.eliminarEvaluacionById(id);

        log.info("Evaluacion eliminada con exito");

        return ResponseDataDto.builder()
                .message("Evaluación eliminada correctamente!")
                .code(ResponseDataDto.SUCCESS)
                .build();
    }

    /**
     * consultar las evuaciones para calificar, por materia
     * @param idBimestre id del bimestre
     * @param estadoEvaluacion estado de la evaluacion
     * @param idsMaterias los IDs de las materias separados por coma
     * @return List<EvaluacionDto>
     */
    @Override
    public List<EvaluacionesMateriaDto> obtenerEvaluacionesCalificar(Long idBimestre, Integer estadoEvaluacion, List<Long> idsMaterias) {

        log.info("obtener evaluaciones Calificar: bimestre->" + idBimestre + ", estadoEvaluacion->" + estadoEvaluacion + ", " +
                "idsMaterias->" + Arrays.toString(idsMaterias.toArray()));

        String idsMateriasAsignadas = idsMaterias
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        //lista de evaluaciones a devolver con las evaluaciones por materia
        List<EvaluacionesMateriaDto> evaluacionesMateriaDtos = new ArrayList<>();

        //obtener las evaluaciones a calificar
        List<ConsultaCalificarEvaluacion> evaluaciones = evaluacionRepository.consultaCalificarEvaluaciones(idBimestre, estadoEvaluacion, idsMateriasAsignadas);

        //validar si la lista no tiene datos
        if (Objects.isNull(evaluaciones) || evaluaciones.isEmpty()) {
            return evaluacionesMateriaDtos;
        }

        //obtener los ids de las materias, solo los que sean distintos
        List<Long> idsMateriasDistinct = evaluaciones
                .stream()
                .map(ConsultaCalificarEvaluacion::getIdMateria)
                .distinct()
                .collect(Collectors.toList());

        log.info("ids materias encontradas: " + Arrays.toString(idsMateriasDistinct.toArray()));

        //agregar en la lista cada actividad por materia
        for (Long idMateria : idsMateriasDistinct) {

            log.info("idMateria->" + idMateria);

            EvaluacionesMateriaDto evaluacionesMateriaDto = new EvaluacionesMateriaDto();

            evaluacionesMateriaDto.setMateria(evaluaciones.stream()
                    .filter(a -> a.getIdMateria().equals(idMateria))
                    .findAny()
                    .get()
                    .getMateria());

            evaluacionesMateriaDto.setGradoMateria(evaluaciones.stream()
                    .filter(a -> a.getIdMateria().equals(idMateria))
                    .findAny()
                    .get()
                    .getGradoMateria());

            evaluacionesMateriaDto.setEvaluaciones(evaluaciones.stream()
                    .filter(a -> a.getIdMateria().equals(idMateria))
                    .map(a -> EvaluacionDto.builder()
                            .descripcion(a.getDescripcionEvaluacion())
                            .ponderacion(a.getPonderacionEvaluacion())
                            .idMateriaId(a.getIdMateria())
                            .estado(a.getEstadoEvaluacion())
                            .id(a.getIdEvaluacion())
                            .build())
                    .collect(Collectors.toList()));

            log.info("evaluaciones: " + Arrays.toString(evaluacionesMateriaDto.getEvaluaciones().toArray()));
            evaluacionesMateriaDtos.add(evaluacionesMateriaDto);
        }


        return evaluacionesMateriaDtos;
    }


    /**
     * Obtener la evaluacion a calificar, su descripcion, id, y valor
     *
     * @param id el id de la actividad que se desea obtener
     * @return ActividadDto
     */
    @Override
    public EvaluacionDto obtenerEvaluacionCalificar(Long id) {

        log.info("obtener Evaluacion Calificar: " + id);

        var actividad = evaluacionRepository.obtenerEvaluacionCalificarById(id).orElseThrow();

        return EvaluacionDto.builder()
                .descripcion(actividad.getDescripcion())
                .ponderacion(actividad.getPonderacion())
                .materiaDescripcion(actividad.getDescripcionMateria())
                .id(actividad.getId())
                .idGradoId(actividad.getIdGrado())
                .descripcionGrado(actividad.getDescripcionGrado())
                .build();
    }

    /**
     * @param idEvaluacion el id de la evaluacion que se desea calificar
     * @param idsSeccionesAsignadasUsuario los ids de las secciones asignadas al usuario
     * @return una plantilla con los alumnos de la seccion asignada al usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<CalificacionAlumnoDto> obtenerPlantillaCalificarEvaluacion(Long idEvaluacion, List<Long> idsSeccionesAsignadasUsuario) {

        log.info("construyendo plantilla para calificar evaluacion: idEvaluacion: " + idEvaluacion + ", secciones: " + Arrays.toString(idsSeccionesAsignadasUsuario.toArray()));

        List<ConsultaDetalleCalificacion> calificacionesAlumnos = detalleCalificacionRepository.findByIdEvaluacionIdAndIdAlumnoIdSeccionIdIn(idEvaluacion, idsSeccionesAsignadasUsuario);

        //si hay datos, la evaluacion ya fue calificada, por lo tanto se debe retornar la plantilla con los datos ya calificados
        if(Objects.nonNull(calificacionesAlumnos) && !calificacionesAlumnos.isEmpty()){
            return calificacionesAlumnos.stream()
                    .map(a -> CalificacionAlumnoDto.builder()
                            .id(a.getId())
                            .puntosObtenidos(a.getPuntosObtenidos())
                            .idEvaluacion(a.getIdEvaluacionId())
                            .idAlumno(a.getIdAlumnoId())
                            .fechaCalificacion(a.getFechaCalificacion())
                            .gradoAlumno(a.getIdAlumnoIdSeccionIdGradoDescripcion() + " " + a.getIdAlumnoIdSeccionDescripcion())
                            .nombreAlumno(a.getIdAlumnoNombre() + " " + a.getIdAlumnoApellido())
                            .build())
                    .collect(Collectors.toList());
        }


        List<ConsultaAlumnoCalificacion> alumnos = alumnoRepository.findByEstadoAndIdSeccionIdIn(Alumno.ACTIVO, idsSeccionesAsignadasUsuario);

        //construir la plantilla con los alumnos de la seccion asignada al usuario y retornarla
        return alumnos.stream()
                .map(a -> CalificacionAlumnoDto.builder()
                        .puntosObtenidos(0.0D)
                        .idEvaluacion(idEvaluacion)
                        .idAlumno(a.getId())
                        .fechaCalificacion(LocalDate.now())
                        .gradoAlumno(a.getIdSeccionIdGradoDescripcion() + " " + a.getIdSeccionDescripcion())
                        .nombreAlumno(a.getNombre() + " " + a.getApellido())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Guardar los punteos obtenidos por los alumnos en la evaluacion
     *
     * @param calificacionesAlumno los datos de la calificacion de los alumnos
     * @param evaluacion la evaluacion que se esta calificando
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto guardarCalificacionesEvaluacion(EvaluacionDto evaluacion, List<CalificacionAlumnoDto> calificacionesAlumno) {

        log.info("guardando calificaciones de evaluacion: " + Arrays.toString(calificacionesAlumno.toArray()));

        List<String> errores = new ArrayList<>();

        //validar que el punteo obtenido no sea menor a 0
        for (CalificacionAlumnoDto calificacion : calificacionesAlumno) {
            if (calificacion.getPuntosObtenidos() < 0) {
                errores.add("Uno o mas punteos ingresados son menores a cero. Por favor verifique los datos ingresados.");
                break;
            }
        }

        //validar que el punteo obtenido por el alumno no sea mayor al valor de la evaluacion
        for (CalificacionAlumnoDto calificacion : calificacionesAlumno) {

            if (calificacion.getPuntosObtenidos() > evaluacion.getPonderacion()) {
                errores.add("El alumno " + calificacion.getNombreAlumno() + " ha obtenido " + calificacion.getPuntosObtenidos()
                        + " puntos, pero la evaluación solo vale " + evaluacion.getPonderacion() + " puntos. Por favor verifique los datos ingresados.");
            }
        }

        //si hay errores, retornarlos
        if (!errores.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al guardar las calificaciones de evaluación")
                    .errors(errores)
                    .build();
        }

        //obtener usaurio y bimestre actual de la sesion del usuario
        Usuario user = usuarioRepository.getReferenceById(Objects.requireNonNull(userFacade.getUserSession().getIdUsuario(), "El usuario no puede ser nulo"));
        Bimestre bimestreActual = Objects.requireNonNull(userFacade.getBimestreActual(), "El bimestre actual no puede ser nulo");

        //recorres la lista de alumnos con su calificacion dada por parte del docente
        for (CalificacionAlumnoDto calificacionAlumno : calificacionesAlumno) {

            DetalleCalificacion detalle = new DetalleCalificacion();
            detalle.setId(calificacionAlumno.getId());
            detalle.setPuntosObtenidos(calificacionAlumno.getPuntosObtenidos());
            detalle.setIdEvaluacion(evaluacionRepository.getReferenceById(Objects.requireNonNull(evaluacion.getId(), "El id de la evaluación no puede ser nulo")));
            detalle.setIdAlumno(alumnoRepository.getReferenceById(calificacionAlumno.getIdAlumno()));
            detalle.setFechaCalificacion(LocalDate.now());
            detalle.setIdUsuario(user);
            detalle.setIdBimestre(bimestreActual);

            detalleCalificacionRepository.save(detalle);
        }

        return ResponseDataDto.builder()
                .code(1)
                .message("Evaluación <strong>" + evaluacion.getDescripcion() + "</strong> calificada exitosamente!")
                .data(calificacionesAlumno)
                .build();
    }

}
