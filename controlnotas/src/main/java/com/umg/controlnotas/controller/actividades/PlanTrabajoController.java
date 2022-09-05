package com.umg.controlnotas.controller.actividades;

import com.umg.controlnotas.model.custom.PlanTrabajoJSON;
import com.umg.controlnotas.model.custom.ResponseData;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.services.PlanTrabajoService;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/plan-trabajo")
@Log
public class PlanTrabajoController {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private PlanTrabajoService planTrabajoService;
    @Autowired
    private UserFacade userFacade;


    @GetMapping(value = "/nuevo")
    public String Plan_trabajo(Model model) {

        var bimestre = userFacade.getUserSession().getBimestre();

        //agregar grados sin plan trabajo al model para que se pueda usar en el formulario
        model.addAttribute("grados", gradoRepository.obtenerGradosSinPlanTrabajo(bimestre.getId()));

        return "actividades/nuevo-plan-trabajo";
    }



    /**
     * guardar plan trabajo
     *
     * @param planTrabajoJSON el plan trabajo en formato JSON
     * @return el plan trabajo guardado
     */
    @PostMapping(value = "/registrar")
    @ResponseBody
    public ResponseEntity<ResponseData> guardarPlanTrabajo(@RequestBody PlanTrabajoJSON planTrabajoJSON) {

        log.info("guardarPlanTrabajo: " + planTrabajoJSON);
        ResponseData responseData;

        try {
            //guardar plan trabajo
            responseData = planTrabajoService.guardarPlanTrabajo(planTrabajoJSON);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el plan de trabajo " + e.getMessage()
            );
        }

        return ResponseEntity.ok().body(responseData);
    }


}
