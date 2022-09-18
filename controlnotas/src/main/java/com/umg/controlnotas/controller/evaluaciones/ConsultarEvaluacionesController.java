package com.umg.controlnotas.controller.evaluaciones;

import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.TipoEvaluacionRepository;
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

import java.util.logging.Level;

@Controller
@Log
@RequestMapping(value = "/evaluacion")
public class ConsultarEvaluacionesController {

    @Autowired
    private EvaluacionService evaluacionService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;

    @GetMapping(value = "/consultar")
    public String EvalaucionConsultar(
            @RequestParam(required = false) Integer idTipo,
            @RequestParam(required = false) Long idMateria,
            Model model
    ) {

        log.info("Consultando evaluaciones");
        log.info("idTipo: " + idTipo);
        log.info("idMateria: " + idMateria);


        try {
            model.addAttribute("tipos", tipoEvaluacionRepository.findAll());
            model.addAttribute("materias", userFacade.getAsignacionesUsuario());
            model.addAttribute("idTipo", idTipo);
            model.addAttribute("idMateria", idMateria);
            model.addAttribute("evaluaciones", evaluacionService.consultarEvaluaciones(idMateria, idTipo));
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al consultar evaluaciones", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "evaluaciones/consultar-evaluaciones";
    }


    @DeleteMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> eliminarEvaluacion(@PathVariable("id") Long id) {

        ResponseDataDto responseDataDto;

        log.info("Eliminando evaluacion...");

        try {

            responseDataDto = evaluacionService.eliminarEvaluacion(id);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al eliminar evaluacion", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar evaluacion " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }
}
