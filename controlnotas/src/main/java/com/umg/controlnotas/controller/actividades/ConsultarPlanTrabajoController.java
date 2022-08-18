package com.umg.controlnotas.controller.actividades;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/consultar-plan")
public class ConsultarPlanTrabajoController {

    @GetMapping(value = "/consultar")
    public String ConsultarPlan(){
        return "/actividades/consultar-plan-trabajo";
    }
}
