package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.services.ConductaService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/conducta")
@Log
public class ConductaController {

    private final ConductaService conductaService;
    @Autowired
    public ConductaController(ConductaService conductaService) {
        this.conductaService = conductaService;
    }

    @GetMapping
    public String ConsultarConducta(Model model, Long gradoseccion) {

        log.info("Consultando seccion ID: " + gradoseccion);

        try {

            //obtener los grado y secciones del usuario para listarlos en la vista
            model.addAttribute("gradosSecciones", conductaService.obtenerGradosSeccionesUsuarioSession());
            model.addAttribute("seleccion", gradoseccion);//salvar la seleccion durante la navegacion

            //obtener los alumnos
            model.addAttribute("alumnos", conductaService.obtenerAlumnosConductaSeccion(gradoseccion));


        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return "alumno/conducta";
    }
}
