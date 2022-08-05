package com.umg.controlnotas.controller.alumno;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/registrar-alumno")
public class RegistrarAlumnoController {


    @GetMapping
    public String RegistrarAlumno(Model model){
        model.addAttribute("saludo", "Hola a todos....");
        return "/alumno/registrar-alumno";
    }

}
