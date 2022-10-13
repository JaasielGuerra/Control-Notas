package com.umg.controlnotas.controller.reportes;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.InstitucionService;
import com.umg.controlnotas.services.ReportesService;
import lombok.extern.java.Log;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/reportes")
@Log
public class ReportesController {

    @Autowired
    private InstitucionService institucionService;
    @Autowired
    private ReportesService reportesService;
    @Autowired
    private SeccionRepository seccionRepository;

    @GetMapping(value = "/notas-bimestre")
    public String reporteNotasPorBimestre(Model model) {

        log.info("consultar reporte de notas por bimestre");
        model.addAttribute("institucion", institucionService.getInstitucion(1));

        return "reportes/reporte-notas-bimestre";
    }

    @GetMapping(value = "/notas-finales")
    public String reporteNotasFinales(Model model) {

        log.info("consultar reporte de notas finales");
        model.addAttribute("institucion", institucionService.getInstitucion(1));

        return "reportes/reporte-notas-finales";
    }

    @GetMapping(value = "/alumnos")
    public String reporteAlumnos(Model model, Long seccion, @RequestParam(defaultValue = "false") boolean init) {

        log.info("consultar reporte de alumnos");
        log.info("seccion: " + seccion);
        log.info("init: " + init);

        try {

            model.addAttribute("grados", seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO));
            model.addAttribute("seleccion", seccion); //guardar la seleccion del select

            if (init) {
                model.addAttribute("alumnos", null);
            } else {
                model.addAttribute("institucion", institucionService.getInstitucion(1));
                model.addAttribute("alumnos", reportesService.reporteAlumnos(seccion));
            }

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }
        return "reportes/reporte-alumnos";
    }

    @GetMapping(value = "/actitudinal")
    public String reporteActitudinal(Model model) {

        log.info("consultar reporte actitudinal");

        return "reportes/reporte-actitudinal-alumno";
    }
}
