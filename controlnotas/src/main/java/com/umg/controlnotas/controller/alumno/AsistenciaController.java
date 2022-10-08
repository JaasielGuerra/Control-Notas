package com.umg.controlnotas.controller.alumno;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.GradoSeccion;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.AsistenciaService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/asistencia")
@Log
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;
    @Autowired
    private SeccionRepository seccionRepository;


    @Autowired
    private GradoRepository gradoRepository;

    @GetMapping
    public String ControlAsistencia(Model model, Long idSeccion, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        log.info("Consultando asistencia de seccion: " + idSeccion + " fecha: " + fecha);

        List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);
        model.addAttribute("grados", gradosSecciones);
        model.addAttribute("idSeccion", idSeccion);
        model.addAttribute("fecha", Objects.isNull(fecha) ? LocalDate.now() : fecha);
        model.addAttribute("asistencia", asistenciaService.consultarListadoAsistencia(idSeccion, fecha));

        return "alumno/consultar-asistencia";
    }

    @GetMapping(value = "/nuevo")
    public String NuevoListado(Model model) {

        model.addAttribute("grados", gradoRepository.findByEstado(Grado.ACTIVO));

        return "alumno/nuevo-listado";
    }

    @GetMapping(value = "/detalle")
    public String DetalleAsistencia() {
        return "alumno/detalle-asistencia";
    }

    @GetMapping(value = "/tomar")
    public String TomarAssitencia() {
        return "alumno/tomar-asistencia";
    }

    @PostMapping(value = "/registrar-listado")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarListadoAsistencia(@RequestBody ListadoAsistenciaDto listadoAsistenciaDto) {

        ResponseDataDto responseDataDto;

        log.info("Registrando nuevo listado...");

        try {

            responseDataDto = asistenciaService.registrarNuevaPlantillaListadoAsistencia(listadoAsistenciaDto);

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "Error al registrar nuevo listado", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar listado de asistencia " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }


}
