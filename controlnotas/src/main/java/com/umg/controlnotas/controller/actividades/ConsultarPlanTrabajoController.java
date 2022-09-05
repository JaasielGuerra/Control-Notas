package com.umg.controlnotas.controller.actividades;

import com.umg.controlnotas.model.PlanTrabajo;
import com.umg.controlnotas.model.custom.GradoDescripcionId;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.repository.PlanTrabajoRepository;
import com.umg.controlnotas.services.PlanTrabajoService;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/plan-trabajo")
public class ConsultarPlanTrabajoController {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

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
}
