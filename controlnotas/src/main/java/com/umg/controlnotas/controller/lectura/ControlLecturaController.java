package com.umg.controlnotas.controller.lectura;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/lectura")
public class ControlLecturaController {

    @GetMapping(value = "/control-lectura")
    public String ControlLectura(){
        return "lectura/crear-control-lectura";
    }
}
