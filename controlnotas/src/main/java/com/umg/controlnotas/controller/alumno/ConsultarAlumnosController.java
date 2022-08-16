package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.AlumnoEditar;
import com.umg.controlnotas.model.custom.GradoSeccion;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.AlumnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        try {

            List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);
            model.addAttribute("grados", gradosSecciones);

            List<AlumnoConsultar> alumnos = alumnoService.consultarAlumnos(gradoseccion).orElse(null);
            model.addAttribute("alumnos", alumnos);

            model.addAttribute("seleccion", gradoseccion);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "/alumno/consultar-alumnos";
    }

    @GetMapping(value = "/editar/{id}")
    public String EditarAlumno(@PathVariable Long id, Model model) {
        logger.info("editar alumno ID: " + id);


        AlumnoEditar alumnoEditar = null;

        try {
            alumnoEditar = alumnoService.obtenerAlumnoEditar(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        if (alumnoEditar == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No existe el recurso solicitado"
            );
        }

        model.addAttribute("alumnoEdit", alumnoEditar);

        return "/alumno/editar-alumno";
    }

    @PostMapping("/eliminar")
    public String eliminarAlumno(@ModelAttribute("idAlumno") Long id) {

        logger.info("ID eliminar: " + id);

        try {
            alumnoService.eliminarAlumno(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "redirect:/alumno/consultar";
    }


}
