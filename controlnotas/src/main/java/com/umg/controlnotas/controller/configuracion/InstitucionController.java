package com.umg.controlnotas.controller.configuracion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/institucion")
public class InstitucionController {

    @GetMapping(value = "/configurar")
    public String InstitucionDatos(){
        return "configuraciones/datos-institucion";
    }
}
