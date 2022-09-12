package com.umg.controlnotas.controller.alumno;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/asistencia")
public class AsistenciaController {

    @GetMapping(value = "/control")
    public String ControlAsistencia(){
        return "alumno/consultar-asistencia";
    }

    @GetMapping(value = "/nuevo")
    public String NuevoListado(){
        return "alumno/nuevo-listado";
    }

    @GetMapping(value = "/detalle")
    public String DetalleAsistencia(){
        return "alumno/detalle-asistencia";
    }

    @GetMapping(value = "/tomar")
    public String TomarAssitencia(){
        return "alumno/tomar-asistencia";
    }
}
