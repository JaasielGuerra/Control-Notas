package com.umg.controlnotas.controller.actividades;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/plan-trabajo")
public class PlanTrabajoController {

    @GetMapping(value = "/nuevo")
    public String Plan_trabajo(){
        return "actividades/nuevo-plan-trabajo";
    }

}
