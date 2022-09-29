package com.umg.controlnotas.controller.configuracion;

import com.umg.controlnotas.model.dto.BimestreDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.services.BimestreService;
import com.umg.controlnotas.services.CicloEscolarService;
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
    private CicloEscolarService cicloEscolarService;

    @GetMapping(value = "/ciclo")
    public String GestionarCiclo(Model model) {

        model.addAttribute("cicloActual", cicloEscolarService.obtenerCicloActual());
        model.addAttribute("bimestres", bimestreService.obtenerTodosBimestres());

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

    @PutMapping("/cerrar-bimestre/{idBimestre}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> cerrarBimestre(@PathVariable Long idBimestre) {

        ResponseDataDto responseDataDto;

        log.info("Cerrando bimestre...");

        try {

            responseDataDto = bimestreService.cerrarBimestre(idBimestre);

        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al cerrar bimestre " + ex.getMessage()
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }

    @GetMapping("/ciclos-anteriores")
    public String ciclosAnteriores(Model model) {

        log.info("consultando ciclos anteriores...");

        try {
            model.addAttribute("ciclosAnteriores", cicloEscolarService.obtenerCiclosAnteriores());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar ciclos anteriores " + ex.getMessage(), ex
            );
        }

        return "configuraciones/dialogo-ciclo/include/tabla-ciclos-anteriores";
    }

    @PutMapping("/cerrar-ciclo/{idCiclo}")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> cerrarCiclo(@PathVariable Long idCiclo) {

        ResponseDataDto responseDataDto;

        log.info("Cerrando ciclo...");

        try {

            responseDataDto = cicloEscolarService.cerrarCiclo(idCiclo);

        } catch (Exception ex) {
            log.log(java.util.logging.Level.SEVERE, "Error al cerrar ciclo", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al cerrar ciclo " + ex.getMessage(), ex
            );
        }

        return ResponseEntity.ok(responseDataDto);
    }

}
