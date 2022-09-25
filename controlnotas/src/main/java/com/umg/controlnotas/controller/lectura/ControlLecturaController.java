package com.umg.controlnotas.controller.lectura;

import com.umg.controlnotas.model.dto.AlumnoLecturaDto;
import com.umg.controlnotas.model.dto.DetalleLecturaDto;
import com.umg.controlnotas.model.dto.RegistroDetalleLecturaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.LecturaService;
import com.umg.controlnotas.services.LibroService;
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
import java.util.Objects;

@Controller
@RequestMapping(value = "/control-lectura")
@Log
public class ControlLecturaController {

    @Autowired
    private LecturaService lecturaService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private LibroService libroService;

    @GetMapping
    public String ControlLectura(Model model) {

        log.info("consultando grado para lectura ...");

        try {
            model.addAttribute("grados", lecturaService.obtenerGradosLectura());
        } catch (Exception e) {

            log.log(java.util.logging.Level.SEVERE, "Error al consultar grados para lectura", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar grados para lectura", e);

        }
        return "lectura/control-lectura";
    }

    @GetMapping(value = "/listar-alumnos")
    public String AlumnosLectura(Model model, @RequestParam("idGrado") long idGrado) {

        log.info("consultando alumnos para lectura ...");

        try {
            model.addAttribute("alumnos", lecturaService.obtenerAlumnosLectura(idGrado));
            model.addAttribute("idGrado", idGrado);
        } catch (Exception e) {

            log.log(java.util.logging.Level.SEVERE, "Error al consultar alumnos para lectura", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar alumnos para lectura", e);

        }
        return "lectura/include/alumnos-lectura";
    }

    @GetMapping(value = "/obtener-alumno/{idAlumno}")
    public String AlumnoLectura(Model model, @PathVariable("idAlumno") long idAlumno) {

        log.info("consultando alumno para lectura ...");

        try {

            AlumnoLecturaDto alumno = lecturaService.obtenerAlumnoLectura(idAlumno);
            List<DetalleLecturaDto> detalleLecturaDtos = lecturaService.obtenerDetalleLectura(
                    idAlumno,
                    Objects.requireNonNull(userFacade.getBimestreActual().getId(), "id bimestre no puede ser nulo")
            );

            model.addAttribute("alumno", alumno);
            model.addAttribute("detallesLectura", detalleLecturaDtos);

            //si el alumno no tiene libro, se carga el listado de libro para poder asignarle uno
            if (alumno.getTieneLibro() == 0) {
                model.addAttribute("libros", libroService.consultarLibrosDisponibles());
            }

        } catch (Exception e) {

            log.log(java.util.logging.Level.SEVERE, "Error al consultar alumno para lectura", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar alumno para lectura", e);

        }
        return "lectura/dialogo-control-lectura/include/campos-registro-lectura";
    }

    @PostMapping(value = "/registrar-operacion")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarLectura(@RequestBody RegistroDetalleLecturaDto detalleLecturaDto) {

        log.info("registrando operacion lectura ...");

        ResponseDataDto responseDataDto;

        try {

            Long idBimestre = Objects.requireNonNull(userFacade.getBimestreActual().getId(), "id bimestre no puede ser nulo");
            responseDataDto = lecturaService.registrarDetalleLectura(detalleLecturaDto, idBimestre);


        } catch (Exception e) {

            log.log(java.util.logging.Level.SEVERE, "Error al registrar operacion lectura", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar operacion lectura", e);

        }

        return ResponseEntity.ok(responseDataDto);
    }
}
