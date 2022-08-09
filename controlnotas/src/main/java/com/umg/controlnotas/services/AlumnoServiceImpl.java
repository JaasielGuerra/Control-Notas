package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

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
        a.setNombre(alumno.getNombre());
        a.setApellido(alumno.getApellido());
        a.setDireccion(alumno.getDireccion());
        a.setFechaNacimiento(alumno.getNacimiento());
        a.setCodigoAlumno(alumno.getCodigo());
        a.setEstado(Alumno.ACTIVO);
        a.setFechaCommit(LocalDate.now());
        a.setHoraCommit(LocalTime.now());
        a.setIdSeccion(seccion);
        a.setIdUsuario(usuario);

        alumnoRepository.save(a);
    }

}
