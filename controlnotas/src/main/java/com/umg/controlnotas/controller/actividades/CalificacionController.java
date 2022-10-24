package com.umg.controlnotas.controller.actividades;

import com.umg.controlnotas.model.Actividad;
import com.umg.controlnotas.model.dto.*;
import com.umg.controlnotas.services.ActividadesServicio;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/actividades")
@Log
public class CalificacionController {

    @Autowired
    private ActividadesServicio actividadesServicio;
    @Autowired
    private UserFacade userFacade;


    /**
     * Metodo para consultar las actividades que se pueden calificar por parte del docente
     *
     * @param model
     * @return
     */
    @GetMapping
    public String CalificarActividades(Model model) {

        try {

            Long idBimestre = userFacade.getBimestreActual().getId();

            // obtener los id de las materias que el usuario en sesion tiene asignadas
            List<Long> idsMateriasAsignadas = userFacade.getAsignacionesUsuario()
                    .stream()
                    .map(AsignacionUsuarioDto::getIdMateria)
                    .collect(Collectors.toList());

            log.info("idsMateriasAsignadas: " + Arrays.toString(idsMateriasAsignadas.toArray()));

            // se consultan las actividades de las materias asignadas que esten en estado 1 y que corresponden al bimestre actual
            List<ActividadesMateriaDto> actividades = actividadesServicio.obtenerActividadesCalificar(idBimestre, Actividad.ACTIVO, idsMateriasAsignadas);
            model.addAttribute("actividades", actividades);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al consultar califacion de actividades", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "actividades/calificar-actividades";
    }

    /**
     * navegar hacia la vista que permite calificar una actividad y listar a los alumnos de las secciones
     * que el usuario en sesion tiene asignadas
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/calificar/{idActividad}")
    public String calificarActividadAlumnos(Model model, @PathVariable Long idActividad) {

        try {

            log.info("idActividad a calificar: " + idActividad);


            //obtener la activiadad a calificar por su id
            ActividadDto actividad = actividadesServicio.obtenerActividadCalificar(idActividad);

            log.info("actividad: " + actividad.getDescripcionActividad());
            log.info("valor: " + actividad.getValorActividad());
            log.info("id: " + actividad.getId());

            List<Long> seccionesUsuario = userFacade.obtenerIdsSecciones(actividad.getIdGrado());
            List<CalificacionAlumnoDto> plantilla = actividadesServicio.obtenerPlantillaCalificarActividad(idActividad, seccionesUsuario);

            //poner los datos en el modelo para mostrarlos en la vista
            model.addAttribute("actividad", actividad);
            model.addAttribute("plantilla", plantilla);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al consultar califacion de actividades", ex);

            if (ex instanceof NoSuchElementException) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el recurso solicitado", ex
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return "actividades/calificar-actividad-alumnos";
    }

    @PostMapping(value = "/calificar/guardar-calificacion")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> guardarCalificacionActividad(
            @RequestBody ActividadDto actividadCalificar
    ) {

        ResponseDataDto response;

        try {

            log.info("calificar actividad: " + actividadCalificar.getDescripcionActividad());

            response = actividadesServicio.guardarCalificacionesActividad(actividadCalificar, actividadCalificar.getCalificacionesAlumnos());

        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error al calificar actividad", ex);

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(response);
    }
}
