package com.umg.controlnotas.services;

import com.umg.controlnotas.model.DetalleListado;
import com.umg.controlnotas.model.ListadoAsistencia;
import com.umg.controlnotas.model.dto.DetalleListadoDto;
import com.umg.controlnotas.model.dto.ListadoAsistenciaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
public class AsistenciaServiceImpl implements AsistenciaService {


    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private ListadoAsistenciaRepository listadoAsistenciaRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private DetalleListadoRepository detalleListadoRepository;

    /**
     * Registrar una nueva plantilla listado de asistencia
     *
     * @param listadoAsistenciaDto Objeto con los datos del listado de asistencia
     * @return Objeto con la respuesta de la operación
     */
    @Override
    @Transactional
    public ResponseDataDto registrarNuevaPlantillaListadoAsistencia(ListadoAsistenciaDto listadoAsistenciaDto) {

        List<Long> alumnos = alumnoRepository.findIdAlumnosActivosBySeccion(listadoAsistenciaDto.getIdSeccion());

        // validar que existan alumnos
        if (Objects.isNull(alumnos) || alumnos.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(0)
                    .message("No existen alumnos activos en la sección seleccionada")
                    .build();
        }

        ListadoAsistencia listado = new ListadoAsistencia();
        listado.setObservacion(listadoAsistenciaDto.getObservacion());
        listado.setFecha(listadoAsistenciaDto.getFecha());
        listado.setIdSeccion(seccionRepository.getReferenceById(listadoAsistenciaDto.getIdSeccion()));
        listado.setEstado(ListadoAsistencia.ESTADO_ACTIVO);
        listado.setFechaCommit(LocalDate.now());
        listado.setHoraCommit(LocalTime.now());
        listado.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));
        listado.setIdBimestre(Objects.requireNonNull(userFacade.getBimestreActual(), "El bimestre no puede ser null"));
        listado.setTipo(listadoAsistenciaDto.getTipo());

        listadoAsistenciaRepository.save(listado);


        //recorrer alumnos, y contruis plantilla de tipo detalle_listado
        List<DetalleListado> plantillaListadoAsistencia = alumnos.stream()
                .map(alumno -> {
                    DetalleListado detalle = new DetalleListado();
                    detalle.setIdAlumno(alumnoRepository.getReferenceById(alumno));
                    detalle.setIdListadoAsistencia(listado);
                    detalle.setIdBimestre(Objects.requireNonNull(userFacade.getBimestreActual(), "El bimestre no puede ser null"));
                    return detalle;
                })
                .collect(Collectors.toList());

        detalleListadoRepository.saveAll(plantillaListadoAsistencia);

        //dar el ID recien creado
        listadoAsistenciaDto.setId(listado.getId());

        log.info("Plantilla Listado de asistencia registrado con exito");

        return ResponseDataDto.builder()
                .code(1)
                .data(listadoAsistenciaDto)
                .message("Listado de asistencia registrado correctamente")
                .build();
    }

    /**
     * Consultar listado de asistencia por sección y fecha
     *
     * @param idSeccion Identificador de la sección
     * @param fecha     Fecha del listado de asistencia
     * @return Lista de listados de asistencia
     */
    @Override
    public List<ListadoAsistenciaDto> consultarListadoAsistencia(Long idSeccion, LocalDate fecha) {

        Long idBimestre = userFacade.getBimestreActual().getId();
        List<ListadoAsistenciaDto> listadoAsistencia = new ArrayList<>();

        //si la seccion es null, y la fecha es null, se consulta por la fecha actual
        if (Objects.isNull(idSeccion) && Objects.isNull(fecha)) {
            listadoAsistencia = listadoAsistenciaRepository.findByIdBimestreIdAndEstadoAndFechaOrderByIdDesc(idBimestre, ListadoAsistencia.ESTADO_ACTIVO, LocalDate.now())
                    .stream()
                    .map(ListadoAsistenciaDto::from)
                    .collect(Collectors.toList());
        }

        //si la seccion no es null, y la fecha es null, se consulta por la fecha actual y idSeccion
        if (Objects.nonNull(idSeccion) && Objects.isNull(fecha)) {
            listadoAsistencia = listadoAsistenciaRepository.findByIdBimestreIdAndEstadoAndIdSeccionIdAndFechaOrderByIdDesc(idBimestre, ListadoAsistencia.ESTADO_ACTIVO, idSeccion, LocalDate.now())
                    .stream()
                    .map(ListadoAsistenciaDto::from)
                    .collect(Collectors.toList());
        }

        //si la seccion es null, y la fecha no es null, se consulta por la fecha
        if (Objects.isNull(idSeccion) && Objects.nonNull(fecha)) {
            listadoAsistencia = listadoAsistenciaRepository.findByIdBimestreIdAndEstadoAndFechaOrderByIdDesc(idBimestre, ListadoAsistencia.ESTADO_ACTIVO, fecha)
                    .stream()
                    .map(ListadoAsistenciaDto::from)
                    .collect(Collectors.toList());
        }

        //si la seccion no es null, y la fecha no es null, se consulta por la fecha y idSeccion
        if (Objects.nonNull(idSeccion) && Objects.nonNull(fecha)) {
            listadoAsistencia = listadoAsistenciaRepository.findByIdBimestreIdAndEstadoAndIdSeccionIdAndFechaOrderByIdDesc(idBimestre, ListadoAsistencia.ESTADO_ACTIVO, idSeccion, fecha)
                    .stream()
                    .map(ListadoAsistenciaDto::from)
                    .collect(Collectors.toList());
        }


        return listadoAsistencia;
    }

    /**
     * Consultar listado de asistencia por id
     *
     * @param id Identificador del listado de asistencia
     * @return Listado de asistencia
     */
    @Override
    public ListadoAsistenciaDto consultarListadoAsistenciaPorId(Long id) {
        return ListadoAsistenciaDto.from(listadoAsistenciaRepository.findByEstadoAndId(ListadoAsistencia.ESTADO_ACTIVO, id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el listado de asistencia")));
    }

    /**
     * Consultar la plantilla de un listado de asistencia
     *
     * @param idListadoAsistencia Identificador del listado de asistencia
     * @return Lista de la plantilla de detalle de listado de asistencia
     */
    @Override
    public List<DetalleListadoDto> consultarPlantillaListadoAsistencia(Long idListadoAsistencia) {
        return detalleListadoRepository.findByIdListadoAsistenciaIdOrderByIdAlumnoNombre(idListadoAsistencia)
                .stream()
                .map(DetalleListadoDto::from)
                .collect(Collectors.toList());
    }

    /**
     * Guardar el listado de asistencia
     *
     * @param idListado            Identificador del listado de asistencia
     * @param listadoAsistenciaDto Listado de asistencia
     * @return Listado de asistencia
     */
    @Override
    @Transactional
    public ResponseDataDto guardarAsistencia(Long idListado, ListadoAsistenciaDto listadoAsistenciaDto) {

        //validar la longitud del campo observacion
        if (Objects.nonNull(listadoAsistenciaDto.getObservacion()) && listadoAsistenciaDto.getObservacion().length() > 255) {
            return ResponseDataDto.builder()
                    .code(0)
                    .data(null)
                    .message("La longitud del campo observación no puede ser mayor a 255 caracteres")
                    .build();
        }

        //validar la longitud del campo observacion de cada item
        for (DetalleListadoDto detalle : listadoAsistenciaDto.getPlantillaDetalleListado()) {
            if (Objects.nonNull(detalle.getObservacion()) && detalle.getObservacion().length() > 255) {
                return ResponseDataDto.builder()
                        .code(0)
                        .data(null)
                        .message("La longitud del campo observación no puede ser mayor a 255 caracteres")
                        .build();
            }
        }

        //primero guardar la observacion del listado
        listadoAsistenciaRepository.updateObservacionListadoAsistencia(idListado, listadoAsistenciaDto.getObservacion());


        //ahora recorrer la plantilla detalle lista y actualizar cada item
        listadoAsistenciaDto.getPlantillaDetalleListado().forEach(detalle -> {

            log.info("detalle:" + detalle);
            detalleListadoRepository.updateDetalleListado(detalle.getId(), detalle.getTemperatura(), detalle.getMotivo(), detalle.getObservacion());

        });


        return ResponseDataDto.builder()
                .code(1)
                .message("Listado de asistencia guardado correctamente")
                .data(listadoAsistenciaDto)
                .build();
    }

    /**
     * Eliminar un listado de asistencia por su ID
     *
     * @param idListado Identificador del listado de asistencia
     * @return
     */
    @Override
    @Transactional
    public ResponseDataDto eliminarListadoAsistencia(Long idListado) {

        listadoAsistenciaRepository.updateEstadoListadoAsistencia(idListado, ListadoAsistencia.ESTADO_ELIMINADO);

        return ResponseDataDto.builder()
                .code(1)
                .message("Listado de asistencia eliminado correctamente")
                .data(null)
                .build();
    }
}
