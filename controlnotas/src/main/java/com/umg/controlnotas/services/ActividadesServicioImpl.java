package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.DetalleCalificacion;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.dto.ActividadDto;
import com.umg.controlnotas.model.dto.ActividadesMateriaDto;
import com.umg.controlnotas.model.dto.CalificacionAlumnoDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.ConsultaAlumnoCalificacion;
import com.umg.controlnotas.model.query.ConsultaCalificarActividad;
import com.umg.controlnotas.model.query.ConsultaDetalleCalificacion;
import com.umg.controlnotas.repository.ActividadRepository;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.DetalleCalificacionRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
public class ActividadesServicioImpl implements ActividadesServicio {

    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private DetalleCalificacionRepository detalleCalificacionRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * @param idBimestre
     * @param estadoActividad
     * @param idsMaterias     los IDs de las materias separados por coma
     * @return
     */
    @Override
    public List<ActividadesMateriaDto> obtenerActividadesCalificar(Long idBimestre, Integer estadoActividad, List<Long> idsMaterias) {

        log.info("obtenerActividadesCalificar: " + idBimestre + ", " + estadoActividad + ", " + Arrays.toString(idsMaterias.toArray()));

        String idsMateriasAsignadas = idsMaterias
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        List<ActividadesMateriaDto> actividadesMateriaDtos = new ArrayList<>();

        List<ConsultaCalificarActividad> actividades = actividadRepository.consultaCalificarActividades(idBimestre, estadoActividad, idsMateriasAsignadas);

        if (Objects.isNull(actividades) || actividades.isEmpty()) {
            return actividadesMateriaDtos;
        }

        //agregar en la lista cada actividad por materia
        for (Long idMateria : idsMaterias) {

            ActividadesMateriaDto actividadesMateriaDto = new ActividadesMateriaDto();

            actividadesMateriaDto.setMateria(actividades.stream()
                    .filter(a -> a.getid_materia().equals(idMateria))
                    .findAny()
                    .get()
                    .getmateria());

            actividadesMateriaDto.setGradoMateria(actividades.stream()
                    .filter(a -> a.getid_materia().equals(idMateria))
                    .findAny()
                    .get()
                    .getgrado_materia());

            actividadesMateriaDto.setActividades(actividades.stream()
                    .filter(a -> a.getid_materia().equals(idMateria))
                    .map(a -> ActividadDto.builder()
                            .descripcionActividad(a.getdescripcion_actividad())
                            .valorActividad(a.getvalor_actividad())
                            .idMateria(a.getid_materia())
                            .estado(a.getestado_actividad())
                            .id(a.getid_actividad())
                            .build())
                    .collect(Collectors.toList()));

            log.info("actividaddes: " + Arrays.toString(actividadesMateriaDto.getActividades().toArray()));
            actividadesMateriaDtos.add(actividadesMateriaDto);
        }


        return actividadesMateriaDtos;
    }

    /**
     * Obtener la actividad a calificar, su descripcion, id, y valor
     *
     * @param id el id de la actividad que se desea obtener
     * @return ActividadDto
     */
    @Override
    public ActividadDto obtenerActividadCalificar(Long id) {

        log.info("obtenerActividadCalificar: " + id);

        var actividad = actividadRepository.obtenerActividadCalificarById(id).orElseThrow();

        return ActividadDto.builder()
                .descripcionActividad(actividad.getDescripcionActividad())
                .valorActividad(actividad.getValorActividad())
                .descripcionMateria(actividad.getDescripcionMateria())
                .id(actividad.getId())
                .build();
    }

    /**
     * @param idActividad                  el id de la actividad que se desea calificar
     * @param idsSeccionesAsignadasUsuario los ids de las secciones asignadas al usuario
     * @return una plantilla con los alumnos de la seccion asignada al usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<CalificacionAlumnoDto> obtenerPlantillaCalificarActividad(Long idActividad, List<Long> idsSeccionesAsignadasUsuario) {

        log.info("construyendo plantilla para calificar actividad: " + idActividad + ", secciones: " + Arrays.toString(idsSeccionesAsignadasUsuario.toArray()));

        List<ConsultaDetalleCalificacion> calificacionesAlumnos = detalleCalificacionRepository.findByIdActividadIdAndIdAlumnoIdSeccionIdIn(idActividad, idsSeccionesAsignadasUsuario);

        //si hay datos, la actividad ya fue calificada, por lo tanto se debe retornar la plantilla con los datos ya calificados
        if(Objects.nonNull(calificacionesAlumnos) && !calificacionesAlumnos.isEmpty()){
            return calificacionesAlumnos.stream()
                    .map(a -> CalificacionAlumnoDto.builder()
                            .id(a.getId())
                            .puntosObtenidos(a.getPuntosObtenidos())
                            .idActividad(a.getIdActividadId())
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
                        .idActividad(idActividad)
                        .idAlumno(a.getId())
                        .fechaCalificacion(LocalDate.now())
                        .gradoAlumno(a.getIdSeccionIdGradoDescripcion() + " " + a.getIdSeccionDescripcion())
                        .nombreAlumno(a.getNombre() + " " + a.getApellido())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Guardar los punteos obtenidos por los alumnos en la actividad
     *
     * @param calificacionesAlumno
     */
    @Override
    @Transactional
    public ResponseDataDto guardarCalificacionesActividad(ActividadDto actividad, List<CalificacionAlumnoDto> calificacionesAlumno) {

        log.info("guardando calificaciones de actividad: " + Arrays.toString(calificacionesAlumno.toArray()));

        List<String> errores = new ArrayList<>();

        //validar que el punteo obtenido no sea menor a 0
        for (CalificacionAlumnoDto calificacion : calificacionesAlumno) {
            if (calificacion.getPuntosObtenidos() < 0) {
                errores.add("Uno o mas punteos ingresados son menores a cero. Por favor verifique los datos ingresados.");
                break;
            }
        }

        //validar que el punteo obtenido por el alumno no sea mayor al valor de la actividad
        for (CalificacionAlumnoDto calificacion : calificacionesAlumno) {

            if (calificacion.getPuntosObtenidos() > actividad.getValorActividad()) {
                errores.add("El alumno " + calificacion.getNombreAlumno() + " ha obtenido " + calificacion.getPuntosObtenidos()
                        + " puntos, pero la actividad solo vale " + actividad.getValorActividad() + " puntos. Por favor verifique los datos ingresados.");
            }
        }

        //si hay errores, retornarlos
        if (!errores.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error al guardar las calificaciones")
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
            detalle.setIdActividad(actividadRepository.getReferenceById(Objects.requireNonNull(actividad.getId(), "El id de la actividad no puede ser nulo")));
            detalle.setIdAlumno(alumnoRepository.getReferenceById(calificacionAlumno.getIdAlumno()));
            detalle.setFechaCalificacion(LocalDate.now());
            detalle.setIdUsuario(user);
            detalle.setIdBimestre(bimestreActual);

            detalleCalificacionRepository.save(detalle);
        }

        return ResponseDataDto.builder()
                .code(1)
                .message("Actividad <strong>" + actividad.getDescripcionActividad() + "</strong> calificada exitosamente!")
                .data(calificacionesAlumno)
                .build();
    }
}
