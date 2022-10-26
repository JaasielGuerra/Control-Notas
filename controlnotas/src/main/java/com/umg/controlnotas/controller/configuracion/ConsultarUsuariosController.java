package com.umg.controlnotas.controller.configuracion;

import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
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
import java.util.logging.Level;

@Controller
@Log
@RequestMapping(value = "/usuarios")
public class ConsultarUsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String ConsultarUsuarios(Model model) {

        try {
            List<UsuarioDto> usuarios = usuarioService.consultarUsuarios();
            model.addAttribute("usuarios", usuarios);

        } catch (Exception ex) {
            log.log(Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage()
            );
        }

        return "configuraciones/consultar-usuarios";
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> desactivarUsuario(@PathVariable Long id) {

        ResponseDataDto responseDataDto;
        log.info("dando de baja a usuario...");

        try {
            responseDataDto = usuarioService.desactivarUsuario(id);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }


    @PutMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> activarUsuario(@PathVariable Long id) {

        ResponseDataDto responseDataDto;
        log.info("activando a usuario...");

        try {
            responseDataDto = usuarioService.activarUsuario(id);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "error: " + ex.getMessage(), ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error: " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }
}
