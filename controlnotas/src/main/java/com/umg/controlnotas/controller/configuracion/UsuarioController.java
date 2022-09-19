package com.umg.controlnotas.controller.configuracion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @GetMapping(value = "/nuevo")
    public String NuevoUsuario(){
        return "configuraciones/registrar-usuario";
    }


    @GetMapping(value = "/consultar")
    public String ConsultarUsuarios(){
        return "configuraciones/consultar-usuarios";
    }

    @GetMapping(value = "/editar")
    public String EditarUsuario(){
        return "configuraciones/editar-usuario";
    }
}
