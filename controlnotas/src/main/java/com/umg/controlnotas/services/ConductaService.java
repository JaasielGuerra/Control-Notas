package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ControlActitudinalDto;
import com.umg.controlnotas.model.dto.ReporteDetalleActitudinalDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.AlumnoConsultar;
import com.umg.controlnotas.model.query.ConsultaAlumnoActitudinal;
import com.umg.controlnotas.model.query.GradoSeccion;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConductaService {
    List<GradoSeccion> obtenerGradosSeccionesUsuarioSession();
    List<AlumnoConsultar> obtenerAlumnosConductaSeccion(Long idSeccion);
    @Transactional
    ResponseDataDto registrarConducta(ControlActitudinalDto controlActitudinal);
    @Transactional(readOnly = true)
    List<ReporteDetalleActitudinalDto> obtenerHistorialActitudinal(Long idAlumno, Long idMateria);

    @Transactional(readOnly = true)
    ConsultaAlumnoActitudinal obtenerDatosAlumnoActitudinal(Long idAlumno, Long idMateria);
}
