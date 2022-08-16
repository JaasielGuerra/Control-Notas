package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.AlumnoEditar;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
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

    @Transactional
    @Override
    public void registrarAlumno(AlumnoJSON alumno) {

        Seccion seccion = seccionRepository.getReferenceById(alumno.getSeccion());
        Usuario usuario = usuarioRepository.getReferenceById(1L);

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

        alumnoRepository.save(a);
    }

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

    @Override
    public AlumnoEditar obtenerAlumnoEditar(Long id) {

        AlumnoEditar alumnoJSON = null;

        if (id != null) {
            alumnoJSON = alumnoRepository.findAlumno(id);
        }

        return alumnoJSON;
    }

}
