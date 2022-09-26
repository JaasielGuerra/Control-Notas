package com.umg.controlnotas.controller.evaluaciones;

import com.umg.controlnotas.model.Evaluacion;
import com.umg.controlnotas.model.dto.*;
import com.umg.controlnotas.services.EvaluacionService;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/evaluacion")
@Log
public class CalificarEvaluacionesController {

    @Autowired
    private EvaluacionService evaluacionService;
    @Autowired
    private UserFacade userFacade;


    /**
     * Metodo para consultar las evaluaciones que se pueden calificar por parte del docente
     * @return vista
     */
    @GetMapping(value = "/calificar")
    public String calificarEvaluaciones(Model model) {

        log.info("calificar evaluaciones...");

        try {

            Long idBimestre = userFacade.getBimestreActual().getId();

            // obtener los id de las materias que el usuario en sesion tiene asignadas
            List<Long> idsMateriasAsignadas = userFacade.getAsignacionesUsuario()
                    .stream()
                    .map(AsignacionUsuarioDto::getIdMateria)
                    .collect(Collectors.toList());

            log.info("ids Materias Asignadas: " + Arrays.toString(idsMateriasAsignadas.toArray()));

            // se consultan las actividades de las materias asignadas que esten en estado 1 y que corresponden al bimestre actual
            List<EvaluacionesMateriaDto> evaluaciones = evaluacionService.obtenerEvaluacionesCalificar(idBimestre, Evaluacion.ESTADO_ACTIVO, idsMateriasAsignadas);
            model.addAttribute("evaluaciones", evaluaciones);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al consultar para calificacion de evaluaciones", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }


        return "evaluaciones/calificar-evaluaciones";
    }

    /**
     * navegar hacia la vista que permite calificar una evaluacion y listar a los alumnos de las secciones
     * que el usuario en sesion tiene asignadas
     *
     * @param model modelo
     * @return vista
     */
    @GetMapping(value = "/calificar/{idEvaluacion}")
    public String calificarEvaluacionAlumnos(Model model, @PathVariable Long idEvaluacion) {

        try {

            log.info("idEvaluacion a calificar: " + idEvaluacion);


            //obtener la evaluacion a calificar por su id
           EvaluacionDto evaluacion = evaluacionService.obtenerEvaluacionCalificar(idEvaluacion);

            log.info("evaluacion: " + evaluacion.getDescripcion());
            log.info("ponderacion: " + evaluacion.getPonderacion());
            log.info("id: " + evaluacion.getId());

            //obtener la plantilla para calificar actividades TODO: filtrar por secciones asignadas de la session del usuario
            List<CalificacionAlumnoDto> plantilla = evaluacionService.obtenerPlantillaCalificarEvaluacion(idEvaluacion, Arrays.asList(2L, 3L));

            //poner los datos en el modelo para mostrarlos en la vista
            model.addAttribute("evaluacion", evaluacion);
            model.addAttribute("plantilla", plantilla);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al consultar califacion de evaluacion", ex);

            if (ex instanceof NoSuchElementException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el recurso solicitado", ex
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return "evaluaciones/calificar-evaluacion-alumnos";
    }

    @PostMapping(value = "/calificar/guardar-calificacion")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> guardarCalificacionEvaluacion(
            @RequestBody EvaluacionDto evaluacionCalificar
    ) {

        ResponseDataDto response;

        try {

            log.info("calificar evaluacion: " + evaluacionCalificar.getDescripcion());

            response = evaluacionService.guardarCalificacionesEvaluacion(evaluacionCalificar, evaluacionCalificar.getCalificacionesAlumnos());

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al calificar evaluacion", ex);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(response);
    }

}
