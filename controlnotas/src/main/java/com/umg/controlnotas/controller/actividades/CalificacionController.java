package com.umg.controlnotas.controller.actividades;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/calificacion")
public class CalificacionController {

    @GetMapping(value = "/actividades")
    public String CalificarActividad(){
        return "/actividades/calificar-actividades";
    }
}
