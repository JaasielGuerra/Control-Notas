package com.umg.controlnotas.controller.evaluaciones;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/evaluacion")
public class EvaluacionController {

    @GetMapping(value = "/calificar")
    public String EvalaucionCalificar(){
        return "evaluaciones/calificar-evaluacion";
    }

    @GetMapping(value = "/crear")
    public String EvalaucionCrear(){
        return "evaluaciones/crear-evaluacion";
    }

    @GetMapping(value = "/consultar")
    public String EvalaucionConsultar(){
        return "evaluaciones/consultar-evaluaciones";
    }

    @GetMapping(value = "/editar")
    public String editarEvaluacion(){
        return "evaluaciones/editar-evaluacion";
    }
}
