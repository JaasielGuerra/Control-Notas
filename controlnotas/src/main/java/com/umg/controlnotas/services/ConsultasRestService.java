package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.query.GradoDescripcionId;
import com.umg.controlnotas.model.query.MateriaDescripcionId;
import com.umg.controlnotas.model.query.SeccionDescripcionId;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/consultas")
@Log
public class ConsultasRestService {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private PlanTrabajoService planTrabajoService;
    @Autowired
    private UserFacade userFacade;

    @GetMapping(value = "/grados")
    public List<GradoDescripcionId> listarGrados() {
        return gradoRepository.findByEstado(Grado.ACTIVO);
    }


    @GetMapping(value = "/secciones")
    public List<SeccionDescripcionId> listarSecciones(@RequestParam long idGrado) {

        return seccionRepository.findByIdGradoId(idGrado);
    }

    /**
     * obtener las materias por grado
     *
     * @param id_grado el id del grado
     * @return listado de materias en formato JSON
     */
    @GetMapping(value = "/materias")
    @ResponseBody
    public ResponseEntity<List<MateriaDescripcionId>> obtenerMateriasPorGrado(@RequestParam("grado") long id_grado) {

        log.info("obtenerMateriasPorGrado: " + id_grado);

        List<MateriaDescripcionId> materias;
        try {
            //obtener materias por grado
            materias = planTrabajoService.obtenerMateriasPorGrado(id_grado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().body(materias);
    }

    /**
     * Obtener los grados sin plan de trabajo
     */
    @GetMapping(value = "/grados-sin-plan")
    @ResponseBody
    public ResponseEntity<List<GradoDescripcionId>> obtenerGradosSinPlanTrabajo() {

        var bimestre = userFacade.getUserSession().getBimestre();

        log.info("obtenerGradosSinPlanTrabajo");

        List<GradoDescripcionId> grados;
        try {
            //obtener grados sin plan de trabajo
            grados = gradoRepository.obtenerGradosSinPlanTrabajo(bimestre.getId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().body(grados);
    }

}
