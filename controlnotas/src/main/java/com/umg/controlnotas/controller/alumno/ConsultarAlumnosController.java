package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.GradoSeccion;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.AlumnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/alumno")
public class ConsultarAlumnosController {

    Logger logger = LoggerFactory.getLogger(AlumnoController.class);
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private AlumnoService alumnoService;

    @GetMapping(value = "/consultar")
    public String consultarAlumnos(Model model, Long gradoseccion) {

        logger.info("Consultando seccion ID: " + gradoseccion);

        List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);
        model.addAttribute("grados", gradosSecciones);

        List<AlumnoConsultar> alumnos = alumnoService.consultarAlumnos(gradoseccion).orElse(null);
        model.addAttribute("alumnos", alumnos);

        model.addAttribute("seleccion", gradoseccion);

        return "/alumno/consultar-alumnos";
    }

    @GetMapping(value = "/editar-alumno")
    public String EditarAlumno() {
        return "/alumno/editar-alumno";
    }


}
