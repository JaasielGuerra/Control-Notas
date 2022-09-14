package com.umg.controlnotas.controller.configuracion;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.dto.BimestreDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.BimestreRepository;
import com.umg.controlnotas.services.BimestreService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/institucion")
@Log
public class GestionCicloController {

    @Autowired
    private BimestreService bimestreService;
    @Autowired
    private BimestreRepository bimestreRepository;

    @GetMapping(value = "/ciclo")
    public String GestionarCiclo(Model model) {

        model.addAttribute("bimestreAperturado", bimestreRepository.existsByEstado(Bimestre.ACTIVO));

        return "configuraciones/gestionar-ciclo";
    }

    @GetMapping("/nuevo-bimestre")
    public String nuevoBimestre(Model model) {

        log.info("aperturar nuevo bimestre");

        try {
            model.addAttribute("ultimoBimestre", bimestreService.obtenerUltimoBimestre());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar ultimo bimestre " + ex.getMessage()
            );
        }

        return "configuraciones/dialogo-bimestre/include/campos-apertura-bimestre";
    }

    @PostMapping("/aperturar-bimestre")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> aperturarBimestre(@RequestBody BimestreDto bimestreDto) {

        ResponseDataDto responseDataDto;

        log.info("Aperturando nuevo bimestre...");

        try {

            responseDataDto = bimestreService.aperturarBimestre(bimestreDto);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al aperturar bimestre " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }
}
