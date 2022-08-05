package com.umg.controlnotas.controller.alumno;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/consultar-alumnos")
public class ConsultarAlumnosController {

    @GetMapping
    public String consultarAlumnos(Model model) {

        model.addAttribute("saludo", "Hola a todos....");

        return "/alumno/consultar-alumnos";
    }


}
