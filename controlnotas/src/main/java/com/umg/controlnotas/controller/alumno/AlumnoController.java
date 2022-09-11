package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.dto.AlumnoDto;
import com.umg.controlnotas.repository.GradoRepository;
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

@Controller
@RequestMapping(value = "/alumno")
public class AlumnoController {

    Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private GradoRepository gradoRepository;

    @GetMapping(value = "/nuevo")
    public String nuevoAlumno(Model model) {

        try {

            model.addAttribute("docmentos", alumnoService.consultarDocumentosChecklist());
            model.addAttribute("grados", gradoRepository.findByEstado(Grado.ACTIVO));

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "alumno/registrar-alumno";
    }

    @PostMapping(value = "/registrar")
    @ResponseBody
    public ResponseEntity<AlumnoDto> RegistrarAlumno(@RequestBody AlumnoDto alumno) {

        try {

            alumnoService.registrarAlumno(alumno);
            logger.info("Alumno registrado con exito");

        } catch (Exception ex) {

            logger.error("error registro alumno: " + ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }


    @PostMapping(value = "/actualizar")
    @ResponseBody
    public ResponseEntity<AlumnoDto> actualizarAlumno(@RequestBody AlumnoDto alumno) {

        logger.info("ID: " + alumno.getId());
        logger.info("feccha: " + alumno.getNacimiento());

        try {
            alumnoService.actualizarAlumno(alumno);
            logger.info("Alumno actualizado con exito");

        } catch (Exception ex) {

            logger.error("error actualizacion alumno: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(alumno);
        }

        return ResponseEntity.ok(alumno);
    }

}
