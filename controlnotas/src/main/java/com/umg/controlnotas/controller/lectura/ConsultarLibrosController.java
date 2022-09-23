package com.umg.controlnotas.controller.lectura;


import com.umg.controlnotas.services.LibroService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;

@Controller
@Log
@RequestMapping(value = "/consultar-libros")
public class ConsultarLibrosController {
@Autowired
private LibroService libroService;



    @GetMapping(value = "/consultar")
    public String ConsultarLibros(Model model){


        try {
            model.addAttribute("libros", libroService.consultarLibros());
        } catch (Exception ex){
            log.log(Level.SEVERE, "Error al consultar los libros", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "error " + ex.getMessage()
            );
        }


        return "lectura/consultar-libros";
    }


    @GetMapping(value = "/editar")
    public String EditarLibro(){
        return "lectura/editar-libro";
    }

}
