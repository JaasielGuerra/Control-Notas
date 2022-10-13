package com.umg.controlnotas.services;

import com.umg.controlnotas.model.query.AlumnoReporte;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportesServiceImpl implements ReportesService {

    @Autowired
    private UserFacade userFacade;
    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * Consultar alumnos para reporte
     *
     * @param seccion
     * @return
     */
    @Override
    public List<AlumnoReporte> reporteAlumnos(Long seccion) {

        Long idCicloActual = userFacade.getCicloActual().getId();
        Long idBimestreActual = userFacade.getBimestreActual().getId();

        return alumnoRepository.consultarReporteAlumnos(seccion, idCicloActual, idBimestreActual);
    }
}
