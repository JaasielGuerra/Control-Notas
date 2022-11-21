package com.umg.controlnotas.controller.actividades;

import com.umg.controlnotas.model.dto.ActividadDto;
import com.umg.controlnotas.model.dto.PlanTrabajoDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
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

import java.util.List;

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
     * @param planTrabajoDto el plan trabajo en formato JSON
     * @return el plan trabajo guardado
     */
    @PostMapping(value = "/registrar")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> guardarPlanTrabajo(@RequestBody PlanTrabajoDto planTrabajoDto) {

        log.info("guardarPlanTrabajo: " + planTrabajoDto);
        ResponseDataDto responseDataDto;

        try {
            //guardar plan trabajo
            responseDataDto = planTrabajoService.guardarPlanTrabajo(planTrabajoDto);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el plan de trabajo " + e.getMessage()
            );
        }

        return ResponseEntity.ok().body(responseDataDto);
    }

    @PostMapping(value = "/validar-actividades")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> validarActividades(@RequestBody List<ActividadDto> actividades) {

        log.info("validando ponderacion actividades");
        ResponseDataDto responseDataDto;

        try {
            //validar actividades
            responseDataDto = planTrabajoService.validarActividades(actividades);
        } catch (Exception e) {
            log.log(java.util.logging.Level.SEVERE, "error: " + e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al validar las actividades " + e.getMessage(), e
            );
        }

        return ResponseEntity.ok().body(responseDataDto);
    }


}
