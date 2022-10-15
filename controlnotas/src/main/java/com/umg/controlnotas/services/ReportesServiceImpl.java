package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.CicloEscolarDto;
import com.umg.controlnotas.model.dto.DetalleNotaBimestreDto;
import com.umg.controlnotas.model.dto.ReporteNotasPorBimestreDto;
import com.umg.controlnotas.model.dto.SubDetalleNotaBimestreDto;
import com.umg.controlnotas.model.query.AlumnoReporte;
import com.umg.controlnotas.model.query.ConsultaReporteNotasBimestre;
import com.umg.controlnotas.model.query.DatosAlumnoReporte;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.web.UserFacade;
import liquibase.repackaged.org.apache.commons.lang3.math.NumberUtils;
import liquibase.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReportesServiceImpl implements ReportesService {

    @Autowired
    private UserFacade userFacade;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private BimestreService bimestreService;

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

    /**
     * Obtener datos alumno
     *
     * @param codAlumno codigo del alumno
     */
    @Override
    public DatosAlumnoReporte obtenerDatosAlumno(String codAlumno, Long idCiclo) {

        return alumnoRepository.consultarDatosAlumno(codAlumno, idCiclo);
    }

    /**
     * Consultar reporte de notas por bimestre
     *
     * @param codAlumno codigo del alumno
     * @param idCiclo   id del ciclo
     */
    @Override
    @Transactional(readOnly = true)
    public ReporteNotasPorBimestreDto reporteNotasPorBimestre(String codAlumno, Long idCiclo) {

        //consultar datos del alumno
        DatosAlumnoReporte datosAlumno = obtenerDatosAlumno(codAlumno, idCiclo);

        //si no hay resultado, retornar null
        if (Objects.isNull(datosAlumno)) {
            return null;
        }

        //obtener los ids de los bimestres del ciclo
        List<Long> idsBimestre = bimestreService.obtenerIdsBimestre(idCiclo);

        //recorrer ids de bimestre y obtener las notas por cada bimestre
        List<DetalleNotaBimestreDto> detallesNotaBimestre = new ArrayList<>();
        for (Long idBimestre : idsBimestre) {

            //consultar por bimestre
            List<ConsultaReporteNotasBimestre> detalles = alumnoRepository.consultarReporteNotasBimestre(idBimestre, datosAlumno.getId());

            //llenar el subdetalle de punteos por materia
            List<SubDetalleNotaBimestreDto> subDetalles = new ArrayList<>();
            for (ConsultaReporteNotasBimestre detalle : detalles) {
                SubDetalleNotaBimestreDto subDetalleItem = SubDetalleNotaBimestreDto.builder()
                        .bimestre(detalle.getBimestre())
                        .materia(detalle.getMateria())
                        .puntosActividades(detalle.getPuntos_actividades())
                        .puntosConducta(detalle.getPuntos_conducta())
                        .puntosEvaluaciones(detalle.getPuntos_evaluaciones())
                        //sumar los tres punteos
                        .puntosTotales(NumberUtils.toDouble(detalle.getPuntos_actividades() + "", 0)
                                + NumberUtils.toDouble(detalle.getPuntos_conducta() + "", 0)
                                + NumberUtils.toDouble(detalle.getPuntos_evaluaciones() + "", 0))
                        .build();
                subDetalles.add(subDetalleItem);
            }

            //llenar el detalle de notas por bimestre
            DetalleNotaBimestreDto detalleItem = DetalleNotaBimestreDto.builder()
                    .descripcionBimestre(detalles.get(0).getBimestre())
                    .subDetalleNotaBimestreDtoList(subDetalles)
                    .build();
            detallesNotaBimestre.add(detalleItem);
        }


        //llenar el reporte
        return ReporteNotasPorBimestreDto.builder()
                .nombreAlumno(datosAlumno.getNombre())
                .apellidoAlumno(datosAlumno.getApellido())
                .gradoSeccionAlumno(datosAlumno.getGradoSeccion())
                .codigoAlumno(datosAlumno.getCodigo())
                .direccionAlumno(datosAlumno.getDireccion())
                .ciclo(datosAlumno.getAnioCiclo())
                .detalleNotas(detallesNotaBimestre)
                .build();
    }
}
