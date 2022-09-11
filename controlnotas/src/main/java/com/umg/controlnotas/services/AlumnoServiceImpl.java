package com.umg.controlnotas.services;

import com.umg.controlnotas.model.*;
import com.umg.controlnotas.model.query.*;
import com.umg.controlnotas.model.dto.AlumnoDto;
import com.umg.controlnotas.model.dto.AsignacionAlumnoDto;
import com.umg.controlnotas.model.dto.PlantillaChecklistDto;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.web.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private DocumentoExpedienteRepository documentoExpedienteRepository;
    @Autowired
    private DetalleExpedienteRepository detalleExpedienteRepository;

    /**
     * Metodo transaccional para registrar un alumnno
     *
     * @param alumno modelo JSON del alumno
     */
    @Transactional
    @Override
    public void registrarAlumno(AlumnoDto alumno) {

        Seccion seccion = seccionRepository.getReferenceById(alumno.getSeccion());
        Usuario usuario = usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario());

        Alumno a = new Alumno();
        a.setNombre(alumno.getNombre().strip());
        a.setApellido(alumno.getApellido().strip());
        a.setDireccion(alumno.getDireccion().strip());
        a.setFechaNacimiento(alumno.getNacimiento());
        a.setCodigoAlumno(alumno.getCodigo().strip());
        a.setEstado(Alumno.ACTIVO);
        a.setFechaCommit(LocalDate.now());
        a.setHoraCommit(LocalTime.now());
        a.setIdSeccion(seccion);
        a.setIdUsuario(usuario);

        if (alumno.getExpediente() != null) {
            a.setEstadoExpediente(alumno.getExpediente());
        }
        a.setObservacionExpediente(alumno.getObservacion());

        alumnoRepository.save(a);

        // registrar detalle de expediente del alumno
        List<DetalleExpediente> detalleExpedienteList = new ArrayList<>();

        // tomar plantillas de checklist y registrar detalle expediente
        if (alumno.getPlantillaChecklistDtos() != null) {
            for (PlantillaChecklistDto plantillaChecklistDto : alumno.getPlantillaChecklistDtos()) {
                DetalleExpediente detalleExpediente = new DetalleExpediente();
                detalleExpediente.setIdAlumno(a);
                detalleExpediente.setEstado(plantillaChecklistDto.getEstado());
                detalleExpediente.setIdDocumentoExpediente(documentoExpedienteRepository.getReferenceById(plantillaChecklistDto.getIdDocumentoExpediente()));
                detalleExpedienteList.add(detalleExpediente);
            }
        }


        detalleExpedienteRepository.saveAll(detalleExpedienteList);
    }

    /**
     * Metodo transaccional para actualizar un alumnno
     *
     * @param alumno modelo JSON del alumno
     */
    @Transactional
    @Override
    public void actualizarAlumno(AlumnoDto alumno) {

        Alumno a = alumnoRepository.findById(alumno.getId()).orElseThrow();
        a.setCodigoAlumno(alumno.getCodigo().strip());
        a.setNombre(alumno.getNombre().strip());
        a.setApellido(alumno.getApellido().strip());
        a.setDireccion(alumno.getDireccion().strip());
        a.setFechaNacimiento(alumno.getNacimiento());

        alumnoRepository.save(a);
    }

    /**
     * Devuelve un listado de alumnos dada su sección, o bien se puede pasar null
     * para obtener un listado de los alumnos que no tienen asignacion
     *
     * @param idSeccion puede ser null para devolver los alumnos sin asignacion
     * @return listado de alumnos
     */
    @Override
    public Optional<List<AlumnoConsultar>> consultarAlumnos(Long idSeccion) {

        List<AlumnoConsultar> alumnos = null;

        if (idSeccion != null && idSeccion > 0) {
            alumnos = alumnoRepository.findAlumnosActivosBySeccion(idSeccion);
        }

        if (idSeccion != null && idSeccion == 0) {
            alumnos = alumnoRepository.findAlumnosActivosNoAsignados();
        }

        if (idSeccion == null) {
            alumnos = alumnoRepository.findAlumnosActivos();
        }

        return Optional.ofNullable(alumnos);
    }

    /**
     * Devuelve un modelo con los datos que son editables del alumno
     *
     * @param id id del alumno
     * @return modelo editable del alumnno
     */
    @Override
    public AlumnoEditar obtenerAlumnoEditar(Long id) {

        AlumnoEditar alumnoJSON = null;

        if (id != null) {
            alumnoJSON = alumnoRepository.findAlumno(id);
        }

        return alumnoJSON;
    }


    /**
     * Metodo transaccional para eliminar un alumno, esto lo que hace es cambiar
     * estado del alumno a INACTIVO = 0
     *
     * @param idAlumno id alumno
     */
    @Transactional
    @Override
    public void eliminarAlumno(long idAlumno) {
        alumnoRepository.eliminar(idAlumno);
    }

    /**
     * Devuelve la asignacion del alumno, y una lista con los grados y secciones
     *
     * @return un modelo con el id seccion y un listado de grado y seccion
     */
    @Override
    public AsignacionAlumnoDto obtenerAsignacion(long idAlumno) {

        var a = new AsignacionAlumnoDto();
        a.setIdSeccionAlumno(alumnoRepository.obtenerIdSeccion(idAlumno));
        a.setGradoSeccionList(seccionRepository.findGradosSeccionesByEstadoGrado(Grado.ACTIVO));
        return a;
    }

    /**
     * Cambiar asignacion de grado y seccion de un alumno
     *
     * @param idSeccion id de asignacion, puede ser null para desasignar
     * @param idAlumno  id alumno a reasignar
     */
    @Transactional
    @Override
    public void cambiarAsignacionAlumno(Long idSeccion, long idAlumno) {


        Seccion seccion = null;

        if (idSeccion != null) {
            seccion = seccionRepository.getReferenceById(idSeccion);
        }

        alumnoRepository.updateSecccion(seccion, idAlumno);
    }

    /**
     * Obtener todos los documentos de un expediente de par en par con el id del expediente
     *
     * @return List<DocumentoChecklist [ ]>
     */
    @Override
    public List<DocumentoChecklist[]> consultarDocumentosChecklist() {

        List<DocumentoChecklist[]> documentos = new ArrayList<>();

        //obtener todos los documentos de checklist
        List<DocumentoChecklist> documentoChecklists = documentoExpedienteRepository.findByEstado(DocumentoExpediente.ACTIVO);

        // llenar de par en par los documentos de checklist en una lista
        for (int i = 0; i < documentoChecklists.size(); i++) {

            DocumentoChecklist[] documentosPar;

            // si el indice es igual al tamaño de la lista menos 1, significa que es el ultimo elemento de la lista
            if (i == documentoChecklists.size() - 1) {

                documentosPar = new DocumentoChecklist[1];

                // se asigna solo el primer elemento de la lista
                documentosPar[0] = documentoChecklists.get(i);
                documentos.add(documentosPar);

                break;
            }

            documentosPar = new DocumentoChecklist[2];

            // si el indice es menor al tamaño de la lista menos 1, significa que no es el ultimo elemento de la lista
            documentosPar[0] = documentoChecklists.get(i);
            documentosPar[1] = documentoChecklists.get(i + 1);

            i++;// incrementar el indice para que no se repita el elemento

            documentos.add(documentosPar);

        }
        return documentos;
    }

    /**
     * Metodo para obtener el detalle de expediente de un alumno
     *
     * @param idAlumno el id del alumno
     * @return un modelo con el detalle del expediente
     */
    @Override
    public List<DetalleExpedienteEditar[]> consultarExpedienteAlumno(long idAlumno) {

        List<DetalleExpedienteEditar[]> detalleExpedienteEditar = new ArrayList<>();


        List<DetalleExpedienteEditar> expedienteAlumnoList = detalleExpedienteRepository.findByIdAlumnoId(idAlumno);

        // llenar de par en par los documentos de checklist en una lista
        for (int i = 0; i < expedienteAlumnoList.size(); i++) {

            DetalleExpedienteEditar[] detalleExpedienteEditarPar;

            // si el indice es igual al tamaño de la lista menos 1, significa que es el ultimo elemento de la lista
            if (i == expedienteAlumnoList.size() - 1) {

                detalleExpedienteEditarPar = new DetalleExpedienteEditar[1];

                // se asigna solo el primer elemento de la lista
                detalleExpedienteEditarPar[0] = expedienteAlumnoList.get(i);
                detalleExpedienteEditar.add(detalleExpedienteEditarPar);

                break;
            }

            detalleExpedienteEditarPar = new DetalleExpedienteEditar[2];
            detalleExpedienteEditarPar[0] = expedienteAlumnoList.get(i);
            detalleExpedienteEditarPar[1] = expedienteAlumnoList.get(i + 1);
            i++;// incrementar el indice para que no se repita el elemento
            detalleExpedienteEditar.add(detalleExpedienteEditarPar);
        }
        return detalleExpedienteEditar;
    }

    /**
     * Obtener los datos del expediente del alumno, asi como su detalle de expediente
     *
     * @param idAlumno id del alumno
     * @return Datos del expediente, estado, observacion y detalle de expediente
     */
    @Override
    public AlumnoDto obtenerDatosExpedienteAlumno(long idAlumno) {

        // Obtener datos del expediente del alumno
        DatosExpediente expedienteAlumnoEditar = alumnoRepository.findByIdAlumno(idAlumno);

        // obtener el detalle del expediente del alumno y mapearlo a un modelo PlantillaChecklist
        List<PlantillaChecklistDto> plantillaChecklistDtos = detalleExpedienteRepository.findByIdAlumnoId(idAlumno)
                .stream().map(d -> {

                    PlantillaChecklistDto plantilla = new PlantillaChecklistDto();
                    plantilla.setEstado(d.getEstado());
                    plantilla.setIdDetalleExpediente(d.getId());
                    plantilla.setDescripcionDocumento(d.getIdDocumentoExpedienteDescripcion());
                    return plantilla;

                }).collect(Collectors.toList());

        // construir el modelo de datos del expediente del alumno
        return AlumnoDto.builder()
                .id(expedienteAlumnoEditar.getIdAlumno())
                .expediente(expedienteAlumnoEditar.getEstadoExpediente())
                .observacion(expedienteAlumnoEditar.getObservacionExpediente())
                .plantillaChecklistDtos(plantillaChecklistDtos)
                .build();
    }

    @Override
    @Transactional
    public void guardarChecklistExpediente(AlumnoDto alumno) {

        alumnoRepository.updateDatosExpediente(alumno.getExpediente(), alumno.getObservacion(), alumno.getId());

        //recorrer la lista de plantillaChecklists y actualizar estado detalleExpediente
        alumno.getPlantillaChecklistDtos().forEach(p -> {
            detalleExpedienteRepository.updateEstado(p.getEstado(), p.getIdDetalleExpediente());
        } );

    }


}
