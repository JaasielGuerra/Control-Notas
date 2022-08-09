package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.services.AlumnoService;
import com.umg.controlnotas.services.AlumnoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/alumno")
public class AlumnoController {

    Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private GradoRepository gradoRepository;

    @GetMapping(value = "/nuevo")
    public String nuevoAlumno(Model model){

        model.addAttribute("grados", gradoRepository.findByEstado(Grado.ACTIVO));

        return "/alumno/registrar-alumno";
    }

    @PostMapping(value = "/registrar")
    @ResponseBody
    public ResponseEntity<AlumnoJSON> RegistrarAlumno(@RequestBody AlumnoJSON alumno){

        try {

            alumnoService.registrarAlumno(alumno);
            logger.info("Alumno registrado con exito");

        }catch (Exception ex){

            logger.error("error registro alumno: " + ex.getMessage());
            return  ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(alumno);
    }

}
