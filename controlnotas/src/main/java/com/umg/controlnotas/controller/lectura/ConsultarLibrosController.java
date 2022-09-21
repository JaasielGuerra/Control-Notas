package com.umg.controlnotas.controller.lectura;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/consultar-libros")
public class ConsultarLibrosController {

    @GetMapping(value = "/consultar")
    public String ConsultarLibros(){
        return "lectura/consultar-libros";
    }


    @GetMapping(value = "/editar")
    public String EditarLibro(){
        return "lectura/editar-libro";
    }

}
