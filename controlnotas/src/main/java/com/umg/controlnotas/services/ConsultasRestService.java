package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Grado;
import com.umg.controlnotas.model.custom.GradoDescripcionId;
import com.umg.controlnotas.model.custom.SeccionDescripcionId;
import com.umg.controlnotas.repository.GradoRepository;
import com.umg.controlnotas.repository.SeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/consultas")
public class ConsultasRestService {

    @Autowired
    private GradoRepository gradoRepository;
    @Autowired
    private SeccionRepository seccionRepository;


    @GetMapping(value = "/grados")
    public List<GradoDescripcionId> listarGrados() {
        return gradoRepository.findByEstado(Grado.ACTIVO);
    }


    @GetMapping(value = "/secciones")
    public List<SeccionDescripcionId> listarSecciones(@RequestParam long idGrado) {

        return seccionRepository.findByIdGradoId(idGrado);
    }

}
