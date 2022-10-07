package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.query.*;
import com.umg.controlnotas.model.dto.AlumnoDto;
import com.umg.controlnotas.model.dto.AsignacionAlumnoDto;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.AlumnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping(value = "/alumno")
public class ConsultarAlumnosController {

    Logger logger = LoggerFactory.getLogger(AlumnoController.class);
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private AlumnoRepository alumnoRepository;

    @GetMapping(value = "/consultar")
    public String consultarAlumnos(Model model, Long gradoseccion) {


        try {

            int countSinExpediente = alumnoRepository.contarAlumnosExpedienteIncompleto();

            logger.info("Consultando seccion ID: " + gradoseccion);
            logger.info("Alumnos sin expediente: " + countSinExpediente);

            List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);
            model.addAttribute("grados", gradosSecciones);

            List<AlumnoConsultar> alumnos = alumnoService.consultarAlumnos(gradoseccion).orElse(null);
            model.addAttribute("alumnos", alumnos);

            model.addAttribute("expedienteIncompleto", countSinExpediente);

            model.addAttribute("seleccion", gradoseccion);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "alumno/consultar-alumnos";
    }

    @GetMapping(value = "/editar/{id}")
    public String EditarAlumno(@PathVariable Long id, Model model) {
        logger.info("editar alumno ID: " + id);


        AlumnoEditar alumnoEditar = null;

        try {
            alumnoEditar = alumnoService.obtenerAlumnoEditar(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        if (alumnoEditar == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No existe el recurso solicitado"
            );
        }

        model.addAttribute("alumnoEdit", alumnoEditar);

        return "alumno/editar-alumno";
    }

    @PostMapping("/eliminar")
    public String eliminarAlumno(@ModelAttribute("idAlumno") Long id) {

        logger.info("ID eliminar: " + id);

        try {
            alumnoService.eliminarAlumno(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "redirect:/alumno/consultar";
    }

    @GetMapping("obtenerAsignacionAlumno")
    @ResponseBody
    public AsignacionAlumnoDto obtenerAsignacion(@RequestParam Long idAlumno) {

        AsignacionAlumnoDto asignacionAlumnoDto = null;

        try {
            logger.info("obteniendo asignacion alumno id: " + idAlumno);
            asignacionAlumnoDto = alumnoService.obtenerAsignacion(idAlumno);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return asignacionAlumnoDto;
    }

    @PostMapping("cambiarAsignacion")
    @ResponseBody
    public ResponseEntity<AsignacionAlumnoDto> cambiarAsignacionAlumno(@RequestBody AsignacionAlumnoDto asignacionAlumnoDto) {

        try {

            logger.info("id alumno: " + asignacionAlumnoDto.getIdAlumno());
            logger.info("id seccion: " + asignacionAlumnoDto.getIdSeccionAlumno());

            alumnoService.cambiarAsignacionAlumno(asignacionAlumnoDto.getIdSeccionAlumno(), asignacionAlumnoDto.getIdAlumno());

            logger.info("Alumno reasignado");

        } catch (Exception ex) {

            logger.error("error reasignando alumno: " + ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().body(asignacionAlumnoDto);
    }

    @GetMapping("/obtenerExpedienteAlumno")
    @ResponseBody
    public ResponseEntity<AlumnoDto> obtenerExpedienteAlumno(@RequestParam Long idAlumno) {

        AlumnoDto expedienteAlumno = null;

        try {
            logger.info("obteniendo expediente alumno id: " + idAlumno);
            expedienteAlumno = alumnoService.obtenerDatosExpedienteAlumno(idAlumno);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(expedienteAlumno);
    }

    @PostMapping(value = "/guardarChecklistExpediente")
    @ResponseBody
    public ResponseEntity<AlumnoDto> RegistrarAlumno(@RequestBody AlumnoDto alumno) {

        try {
            logger.info("guardando expediente alumno id: " + alumno.getId());
            alumnoService.guardarChecklistExpediente(alumno);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }
        return ResponseEntity.ok(alumno);
    }

}
