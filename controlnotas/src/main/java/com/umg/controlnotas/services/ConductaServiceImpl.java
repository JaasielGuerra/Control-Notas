package com.umg.controlnotas.services;

import com.umg.controlnotas.model.query.AlumnoConsultar;
import com.umg.controlnotas.model.query.GradoSeccion;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class ConductaServiceImpl implements ConductaService {

    private final AlumnoRepository alumnoRepository;
    private final SeccionRepository seccionRepository;
    private final UserFacade userFacade;

    @Autowired
    public ConductaServiceImpl(AlumnoRepository alumnoRepository, SeccionRepository seccionRepository, UserFacade userFacade) {
        this.alumnoRepository = alumnoRepository;
        this.seccionRepository = seccionRepository;
        this.userFacade = userFacade;
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
}
