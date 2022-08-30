package com.umg.controlnotas.controller.configuracion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ciclo")
public class GestionCicloController {

    @GetMapping(value = "/gestionar")
    public String GestionarCiclo(){
        return "configuraciones/gestionar-ciclo";
    }
}
