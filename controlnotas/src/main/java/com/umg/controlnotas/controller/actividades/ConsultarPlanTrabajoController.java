package com.umg.controlnotas.controller.actividades;

import com.umg.controlnotas.model.Actividad;
import com.umg.controlnotas.model.PlanTrabajo;
import com.umg.controlnotas.model.custom.*;
import com.umg.controlnotas.repository.ActividadRepository;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.repository.PlanTrabajoRepository;
import com.umg.controlnotas.services.PlanTrabajoService;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log
@Controller
@RequestMapping(value = "/plan-trabajo")
public class ConsultarPlanTrabajoController {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private PlanTrabajoService planTrabajoService;

    @GetMapping(value = "/consultar")
    public String ConsultarPlan(Model model) {

        var idBimestre = userFacade.getUserSession().getBimestre().getId();

        List<GradoDescripcionId> gradosSinPlanTrabajoList = gradoRepository.obtenerGradosSinPlanTrabajo(idBimestre);

        //agregar a la vista los grados sin plan de trabajo
        model.addAttribute("gradosSinPlanTrabajoList", gradosSinPlanTrabajoList);
        //agregar a la vista los plantes de trabajo
        model.addAttribute("planTrabajoList", planTrabajoRepository.findByEstado(PlanTrabajo.ACTIVO));

        return "actividades/consultar-plan-trabajo";
    }

    @GetMapping(value = "/consultar-actividades")
    @ResponseBody
    public List<ActividadEditar> ConsultarActividades(@RequestParam Long idPlanTrabajo) {
        log.info("ConsultarActividades idPlanTrabajo: " + idPlanTrabajo);
        return actividadRepository.findByIdPlanTrabajoIdAndEstado(idPlanTrabajo, Actividad.ACTIVO);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<ResponseData> ActualizarActividadesPlanTrabajo(@PathVariable Long id, @RequestBody List<ActividadJSON> actividades) {

        //imprimir id
        log.info("ActualizarActividadesPlanTrabajo id: " + id);

        actividades.forEach(actividad -> {
            log.info("--------------------------------------------------");
            log.info("id: " + actividad.getId());
            log.info("descripcion: " + actividad.getDescripcionActividad());
            log.info("valor: " + actividad.getValorActividad());
            log.info("idMateria: " + actividad.getIdMateria());
            log.info("estado: " + actividad.getEstado());
        });


        ResponseData responseData;

        try {

            responseData = planTrabajoService.actualizarActividadesPlanTrabajo(id, actividades);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el plan de trabajo " + e.getMessage()
            );
        }

        return ResponseEntity.ok().body(responseData);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<ResponseData> EliminarPlanTrabajo(@PathVariable Long id) {

        log.info("EliminarPlanTrabajo id: " + id);

        ResponseData responseData;

        try {

            responseData = planTrabajoService.eliminarPlanTrabajo(id);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el plan de trabajo " + e.getMessage()
            );
        }

        return ResponseEntity.ok().body(responseData);
    }

}
