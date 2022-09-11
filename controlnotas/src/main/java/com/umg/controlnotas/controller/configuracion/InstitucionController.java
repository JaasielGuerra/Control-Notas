package com.umg.controlnotas.controller.configuracion;

import com.umg.controlnotas.model.dto.InstitucionDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.InstitucionService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Log
@Controller
@RequestMapping(value = "/institucion")
public class InstitucionController {

    @Autowired
    private InstitucionService institucionService;

    @GetMapping
    public String InstitucionDatos(Model model) {
        log.info("consultando datos de la institución");
        model.addAttribute("institucion", institucionService.getInstitucion(1));
        return "configuraciones/datos-institucion";
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> updateInstitucion(@RequestBody InstitucionDto institucionDto, @PathVariable Integer id) {

        ResponseDataDto responseDataDto;

        try {

            responseDataDto = institucionService.actualizarDatosInstitucion(institucionDto, id);

        } catch (Exception e) {
            //lanzar excepcion con error 500
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar institucion", e);
        }

        return ResponseEntity.ok(responseDataDto);
    }
}
