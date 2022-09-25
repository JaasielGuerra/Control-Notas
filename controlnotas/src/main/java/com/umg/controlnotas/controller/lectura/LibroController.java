package com.umg.controlnotas.controller.lectura;

import com.umg.controlnotas.model.dto.LibroDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.LibroService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

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


    @GetMapping(value = "/editar/{id}")
    public String EditarLibro(@PathVariable Long id, Model model) {

        log.info("Editando libro id " + id);

        try {
            LibroDto libroDto = libroService.editarLibro(id);
            model.addAttribute("libro", libroDto);

        } catch (Exception ex) {

            log.log(java.util.logging.Level.SEVERE, "Error al editar libro", ex);

            if(ex instanceof NoSuchElementException){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado", ex
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al editar el libro" + ex.getMessage(), ex
            );
        }

        return "lectura/editar-libro";
    }

    @PutMapping(value = "/actualizar/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> actualizarLibro(@RequestBody LibroDto libro, @PathVariable Long id) {
        ResponseDataDto responseDataDto;
        log.info("Actualizando libro...");

        try {
            responseDataDto = libroService.actualizarLibro(libro, id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el libro" + ex.getMessage(), ex
            );
        }
        return ResponseEntity.ok(responseDataDto);
    }
}



