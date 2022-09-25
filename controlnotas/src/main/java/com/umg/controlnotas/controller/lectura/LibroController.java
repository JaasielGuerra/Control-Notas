package com.umg.controlnotas.controller.lectura;

import com.umg.controlnotas.model.dto.LibroDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.LibroService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/libros")
@Log
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping(value = "/nuevo")
    public String NuevoLibro() {
        return "lectura/registrar-libro";
    }

    @PostMapping(value = "/registrar")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarLibro(@RequestBody LibroDto libro) {
        ResponseDataDto responseDataDto;
        log.info("Registrando libro...");

        try {
            responseDataDto = libroService.registrarNuevoLibro(libro);
        } catch (Exception li) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar el libro" + li.getMessage()
            );
        }
        return ResponseEntity.ok(responseDataDto);
    }
}



