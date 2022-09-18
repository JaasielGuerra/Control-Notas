package com.umg.controlnotas.controller.evaluaciones;

import com.umg.controlnotas.model.dto.EvaluacionDto;
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

@Controller
@RequestMapping(value = "/evaluacion")
@Log
public class EvaluacionController {

    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private EvaluacionService evaluacionService;


    @GetMapping(value = "/calificar")
    public String EvalaucionCalificar() {
        return "evaluaciones/calificar-evaluacion";
    }

    @GetMapping(value = "/crear")
    public String EvalaucionCrear(Model model) {

        model.addAttribute("tipos", tipoEvaluacionRepository.findAll());
        model.addAttribute("materias", userFacade.getAsignacionesUsuario());

        return "evaluaciones/crear-evaluacion";
    }


    @GetMapping(value = "/editar/{id}")
    public String editarEvaluacion(@PathVariable("id") Long id, Model model) {

        log.info("Editar evaluacion: " + id);

        EvaluacionDto evaluacionDto;

        try {
            evaluacionDto = evaluacionService.obtenerEvaluacionEdtitar(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No existe el recurso solicitado"
            );
        }

        model.addAttribute("evaluacion", evaluacionDto);
        model.addAttribute("tipos", tipoEvaluacionRepository.findAll());
        model.addAttribute("materias", userFacade.getAsignacionesUsuario());

        return "evaluaciones/editar-evaluacion";
    }

    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarEvaluacion(@RequestBody EvaluacionDto evaluacion) {

        ResponseDataDto responseDataDto;

        log.info("Registrando evaluacion...");

        try {

            responseDataDto = evaluacionService.registrarNuevaEvaluacion(evaluacion);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar evaluacion " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }

    @PutMapping("/actualizar/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> actualizarEvaluacion(@RequestBody EvaluacionDto evaluacion, @PathVariable("id") Long id) {

        ResponseDataDto responseDataDto;

        log.info("Actualizando evaluacion...");

        try {

            responseDataDto = evaluacionService.actualizarEvaluacion(evaluacion, id);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar evaluacion " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }
}
