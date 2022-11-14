package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.*;
import com.umg.controlnotas.model.query.*;
import com.umg.controlnotas.repository.AlumnoRepository;
import com.umg.controlnotas.web.UserFacade;
import liquibase.repackaged.org.apache.commons.lang3.math.NumberUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
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

    /**
     * Consultar reporte de notas finales
     *
     * @param codAlumno codigo del alumno
     * @param idCiclo   id del ciclo
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseDataDto reporteNotasFinales(String codAlumno, Long idCiclo) {

        //si codigo de alumno, o id de ciclo es nulo, retornar error
        if (Objects.isNull(codAlumno) || Objects.isNull(idCiclo)) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No ha indicado los parámetros requeridos")
                    .build();
        }

        //consultar datos del alumno
        DatosAlumnoReporte datosAlumno = obtenerDatosAlumno(codAlumno, idCiclo);

        //si no hay resultado, retornar null
        if (Objects.isNull(datosAlumno)) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No se encontraron datos del alumno.")
                    .build();
        }

        //obtener los ids de los bimestres del ciclo
        List<Long> idsBimestre = bimestreService.obtenerIdsBimestre(idCiclo);
        log.info("recuento bimestres: " + idsBimestre.size());

        //validar que existan 4 bimestres
        if (idsBimestre.size() < 4) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Las notas finales no se pueden calcular, porque actualmente sólo hay " + idsBimestre.size()
                            + " bimestre(s). Para llevar a cabo el cálculo, deben existir 4 bimestres para el ciclo que se seleccione.")
                    .build();
        }

        if (idsBimestre.size() > 4) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("Error en cálculo de notas finales, hay " + idsBimestre.size() + " bimestres para el ciclo seleccionado, lo cual " +
                            "sobre pasa lo establecido, siendo 4 lo correcto. Por favor contacte al administrador.")
                    .build();
        }

        //obtener las notas finales
        List<ConsultaReporteNotaFinal> notas = alumnoRepository.consultarReporteNotasFinales(idsBimestre.get(0), idsBimestre.get(1), idsBimestre.get(2), idsBimestre.get(3), datosAlumno.getId());
        List<DetalleNotaFinalDto> detallesNotaFinal = new ArrayList<>();
        for (ConsultaReporteNotaFinal nota : notas) {
            DetalleNotaFinalDto detalle = DetalleNotaFinalDto.builder()
                    .materia(nota.getMateria())
                    .bimestre1(nota.getBimestre_1())
                    .bimestre2(nota.getBimestre_2())
                    .bimestre3(nota.getBimestre_3())
                    .bimestre4(nota.getBimestre_4())
                    //calcular promedio
                    .promedio((nota.getBimestre_1() + nota.getBimestre_2() + nota.getBimestre_3() + nota.getBimestre_4()) / 4)
                    .build();
            detallesNotaFinal.add(detalle);
        }


        //llenar el reporte
        ReporteNotasFinalesDto notaFinal = ReporteNotasFinalesDto.builder()
                .nombreAlumno(datosAlumno.getNombre())
                .apellidoAlumno(datosAlumno.getApellido())
                .gradoSeccionAlumno(datosAlumno.getGradoSeccion())
                .codigoAlumno(datosAlumno.getCodigo())
                .direccionAlumno(datosAlumno.getDireccion())
                .ciclo(datosAlumno.getAnioCiclo())
                .detallesNotaFinal(detallesNotaFinal)
                .build();


        return ResponseDataDto.builder()
                .code(1)
                .message("Datos obtenidos correctamente.")
                .data(notaFinal)
                .build();
    }

    /**
     * Reporte de actitudinal alumno
     *
     * @param codAlumno  codigo del alumno
     * @param idBimestre id del ciclo
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseDataDto reporteActitudinalAlumno(String codAlumno, Long idBimestre) {

        //si codigo de alumno, o id bimestre es nulo, retornar error
        if (Objects.isNull(codAlumno) || Objects.isNull(idBimestre)) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No ha indicado los parámetros requeridos")
                    .build();
        }

        //consultar datos del alumno
        DatosAlumnoReporte datosAlumno = obtenerDatosAlumno(codAlumno, null);

        //si no hay resultado, retornar null
        if (Objects.isNull(datosAlumno)) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No se encontraron datos del alumno.")
                    .build();
        }

        //consultar el reporte actitudinal del alumno
        List<ConsultaReporteActitudinalAlumno> actitudinalAlumno = alumnoRepository.consultarReporteActitudinalAlumno(idBimestre, datosAlumno.getId(), null);

        //si no hay datos retornar mensaje
        if (actitudinalAlumno.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("El alumno no tiene registros de actitudinal.")
                    .build();
        }

        //mapear reporte a detalles de actitudinal
        List<ReporteDetalleActitudinalDto> detalles = actitudinalAlumno.stream()
                .map(r -> ReporteDetalleActitudinalDto.builder()
                        .descripcion(r.getDescripcion())
                        .fecha(r.getFecha())
                        .materia(r.getMateria())
                        .puntosRestados(r.getPuntosRestados())
                        .puntosSumados(r.getPuntosSumados())
                        .puntosActuales(r.getPuntosActuales())
                        .build())
                .collect(Collectors.toList());

        ReporteActitudinalAlumnoDto reporte = ReporteActitudinalAlumnoDto.builder()
                .codigoAlumno(datosAlumno.getCodigo())
                .nombreAlumno(datosAlumno.getNombre())
                .apellidoAlumno(datosAlumno.getApellido())
                .gradoSeccionAlumno(datosAlumno.getGradoSeccion())
                .direccionAlumno(datosAlumno.getDireccion())
                .detalles(detalles)
                .build();


        return ResponseDataDto.builder()
                .code(1)
                .message("Datos obtenidos correctamente.")
                .data(reporte)
                .build();
    }
}
