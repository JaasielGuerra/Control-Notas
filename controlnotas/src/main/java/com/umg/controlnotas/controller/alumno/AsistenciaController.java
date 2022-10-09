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

        try {

            List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);
            model.addAttribute("grados", gradosSecciones);
            model.addAttribute("idSeccion", idSeccion);
            model.addAttribute("fecha", Objects.isNull(fecha) ? LocalDate.now() : fecha);
            model.addAttribute("asistencia", asistenciaService.consultarListadoAsistencia(idSeccion, fecha));

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar control de asistencia " + ex.getMessage()
            );
        }
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

    @GetMapping(value = "/{idListado}")
    public String TomarAsistencia(Model model, @PathVariable Long idListado) {

        log.info("Tomando asistencia del listado: " + idListado);

        try {

            model.addAttribute("listado", asistenciaService.consultarListadoAsistenciaPorId(idListado));
            model.addAttribute("plantilla", asistenciaService.consultarPlantillaListadoAsistencia(idListado));

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar ultimo bimestre " + ex.getMessage()
            );
        }
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

    @PutMapping(value = "/guardar-asistencia/{idListado}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> guardarAsistencia(@PathVariable Long idListado, @RequestBody ListadoAsistenciaDto listadoAsistencia) {

        ResponseDataDto responseDataDto;

        log.info("Guardando asistencia del listado: " + idListado);

        try {

            responseDataDto = asistenciaService.guardarAsistencia(idListado, listadoAsistencia);

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "Error al guardar asistencia", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar asistencia " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }


}
