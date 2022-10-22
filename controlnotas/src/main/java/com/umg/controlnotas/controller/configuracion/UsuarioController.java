package com.umg.controlnotas.controller.configuracion;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.Rol;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
import com.umg.controlnotas.model.query.GradoSeccion;
import com.umg.controlnotas.repository.RolRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import com.umg.controlnotas.services.UsuarioService;
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
@RequestMapping(value = "/usuarios")
@Log
public class UsuarioController {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/nuevo")
    public String NuevoUsuario(Model model) {

        try {

            List<GradoSeccion> gradosSecciones = seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO);

            model.addAttribute("roles", rolRepository.findByEstado(Rol.ESTADO_ACTIVO));
            model.addAttribute("grados", gradosSecciones);

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }


        return "configuraciones/registrar-usuario";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarUsuario(@RequestBody UsuarioDto usuarioDto) {

        ResponseDataDto responseDataDto;

        log.info("Registrando nuevo usuario...");

        try {

            responseDataDto = usuarioService.registrarUsuario(usuarioDto);

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar evaluacion " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }


    @GetMapping(value = "/editar")
    public String EditarUsuario() {
        return "configuraciones/editar-usuario";
    }
}
