package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.AlumnoEditar;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import com.umg.controlnotas.model.custom.AsignacionAlumno;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UserFacade userFacade;

    /**
     * Metodo transaccional para registrar un alumnno
     *
     * @param alumno modelo JSON del alumno
     */
    @Transactional
    @Override
    public void registrarAlumno(AlumnoJSON alumno) {

        Seccion seccion = seccionRepository.getReferenceById(alumno.getSeccion());
        Usuario usuario = usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario());

        Alumno a = new Alumno();
        a.setNombre(alumno.getNombre().strip());
        a.setApellido(alumno.getApellido().strip());
        a.setDireccion(alumno.getDireccion().strip());
        a.setFechaNacimiento(alumno.getNacimiento());
        a.setCodigoAlumno(alumno.getCodigo().strip());
        a.setEstado(Alumno.ACTIVO);
        a.setFechaCommit(LocalDate.now());
        a.setHoraCommit(LocalTime.now());
        a.setIdSeccion(seccion);
        a.setIdUsuario(usuario);

        if(alumno.getExpediente() != null) {
            a.setEstadoExpediente(alumno.getExpediente());
        }
        a.setObservacionExpediente(alumno.getObservacion());

        alumnoRepository.save(a);
    }

    /**
     * Metodo transaccional para actualizar un alumnno
     *
     * @param alumno modelo JSON del alumno
     */
    @Transactional
    @Override
    public void actualizarAlumno(AlumnoJSON alumno) {

        Alumno a = alumnoRepository.findById(alumno.getId()).orElseThrow();
        a.setCodigoAlumno(alumno.getCodigo().strip());
        a.setNombre(alumno.getNombre().strip());
        a.setApellido(alumno.getApellido().strip());
        a.setDireccion(alumno.getDireccion().strip());
        a.setFechaNacimiento(alumno.getNacimiento());

        alumnoRepository.save(a);
    }

    /**
     * Devuelve un listado de alumnos dada su sección, o bien se puede pasar null
     * para obtener un listado de los alumnos que no tienen asignacion
     *
     * @param idSeccion puede ser null para devolver los alumnos sin asignacion
     * @return listado de alumnos
     */
    @Override
    public Optional<List<AlumnoConsultar>> consultarAlumnos(Long idSeccion) {

        List<AlumnoConsultar> alumnos = null;

        if (idSeccion != null && idSeccion > 0) {
            alumnos = alumnoRepository.findAlumnosActivosBySeccion(idSeccion);
        }

        if (idSeccion != null && idSeccion == 0) {
            alumnos = alumnoRepository.findAlumnosActivosNoAsignados();
        }

        if (idSeccion == null) {
            alumnos = alumnoRepository.findAlumnosActivos();
        }

        return Optional.ofNullable(alumnos);
    }

    /**
     * Devuelve un modelo con los datos que son editables del alumno
     *
     * @param id id del alumno
     * @return modelo editable del alumnno
     */
    @Override
    public AlumnoEditar obtenerAlumnoEditar(Long id) {

        AlumnoEditar alumnoJSON = null;

        if (id != null) {
            alumnoJSON = alumnoRepository.findAlumno(id);
        }

        return alumnoJSON;
    }


    /**
     * Metodo transaccional para eliminar un alumno, esto lo que hace es cambiar
     * estado del alumno a INACTIVO = 0
     *
     * @param idAlumno id alumno
     */
    @Transactional
    @Override
    public void eliminarAlumno(long idAlumno) {
        alumnoRepository.eliminar(idAlumno);
    }

    /**
     * Devuelve la asignacion del alumno, y una lista con los grados y secciones
     *
     * @return un modelo con el id seccion y un listado de grado y seccion
     */
    @Override
    public AsignacionAlumno obtenerAsignacion(long idAlumno) {

        var a = new AsignacionAlumno();
        a.setIdSeccionAlumno(alumnoRepository.obtenerIdSeccion(idAlumno));
        a.setGradoSeccionList(seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO));
        return a;
    }

    /**
     * Cambiar asignacion de grado y seccion de un alumno
     *
     * @param idSeccion id de asignacion, puede ser null para desasignar
     * @param idAlumno  id alumno a reasignar
     */
    @Transactional
    @Override
    public void cambiarAsignacionAlumno(Long idSeccion, long idAlumno) {


        Seccion seccion = null;

        if (idSeccion != null) {
            seccion = seccionRepository.getReferenceById(idSeccion);
        }

        alumnoRepository.updateSecccion(seccion, idAlumno);
    }

}
