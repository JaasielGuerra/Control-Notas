package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.dto.ControlActitudinalDto;
import com.umg.controlnotas.model.dto.ReporteDetalleActitudinalDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.ConductaService;
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
@RequestMapping(value = "/conducta")
@Log
public class ConductaController {

    private final ConductaService conductaService;
    private final UserFacade userFacade;

    @Autowired
    public ConductaController(ConductaService conductaService, UserFacade userFacade) {
        this.conductaService = conductaService;
        this.userFacade = userFacade;
    }

    @GetMapping
    public String ConsultarConducta(Model model, Long gradoseccion) {

        log.info("Consultando seccion ID: " + gradoseccion);

        try {

            //obtener los grado y secciones del usuario para listarlos en la vista
            model.addAttribute("gradosSecciones", conductaService.obtenerGradosSeccionesUsuarioSession());
            model.addAttribute("seleccion", gradoseccion);//salvar la seleccion durante la navegacion

            //obtener los alumnos
            model.addAttribute("alumnos", conductaService.obtenerAlumnosConductaSeccion(gradoseccion));


        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return "alumno/conducta";
    }

    @PostMapping("/calificar")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDataDto> CalificarConducta(@RequestBody ControlActitudinalDto controlActitudinal) {

        ResponseDataDto responseDataDto = null;

        log.info("Registrando calificaion de conducta...");

        try {
            responseDataDto = conductaService.registrarConducta(controlActitudinal);
            log.info("Calificacion de conducta registrada correctamente");

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar conducta " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }

    @GetMapping("/historial")
    public String HistorialConducta(Model model,
                                    @RequestParam(name = "alumno") Long idAlumno,
                                    @RequestParam(name = "materia") Long idMateria) {

        log.info("Consultando historial conducta");

        try {
            //consultar el reporte actitudinal del alumno
            List<ReporteDetalleActitudinalDto> actitudinalAlumno = conductaService.obtenerHistorialActitudinal(idAlumno, idMateria);
            model.addAttribute("historialConducta", actitudinalAlumno);

            //datos alumno
            model.addAttribute("alumno",conductaService.obtenerDatosAlumnoActitudinal(idAlumno, idMateria));

            //puntos bimestre
            model.addAttribute("puntosBimestre",userFacade.getBimestreActual().getPuntosActitudinal());

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar ultimo bimestre " + ex.getMessage()
            );
        }

        return "alumno/dialogos-conducta/include/contenido-historial-conducta";
    }
}
