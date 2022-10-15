package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ReporteNotasPorBimestreDto;
import com.umg.controlnotas.model.query.AlumnoReporte;
import com.umg.controlnotas.model.query.DatosAlumnoReporte;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReportesService {
    List<AlumnoReporte> reporteAlumnos(Long seccion);

    DatosAlumnoReporte obtenerDatosAlumno(String codAlumno, Long idCiclo);

    @Transactional(readOnly = true)
    ReporteNotasPorBimestreDto reporteNotasPorBimestre(String codAlumno, Long idCiclo);
}
