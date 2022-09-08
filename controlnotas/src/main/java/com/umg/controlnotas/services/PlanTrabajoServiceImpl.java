package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Actividad;
import com.umg.controlnotas.model.PlanTrabajo;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.custom.ActividadJSON;
import com.umg.controlnotas.model.custom.MateriaDescripcionId;
import com.umg.controlnotas.model.custom.PlanTrabajoJSON;
import com.umg.controlnotas.model.custom.ResponseData;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log
public class PlanTrabajoServiceImpl implements PlanTrabajoService {

    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GradoRepository gradoRepository;

    /**
     * Obtener las materias por grado
     */
    @Override
    public List<MateriaDescripcionId> obtenerMateriasPorGrado(long id_grado) {

        //obtener materias por grado
        List<MateriaDescripcionId> materias = materiaRepository.findByIdGradoId(id_grado);

        return materias;
    }


    /**
     * Guardar un plan de trabajo
     *
     * @param planTrabajoJSON el plan de trabajo en formato JSON
     * @return el plan de trabajo guardado
     */
    @Transactional
    @Override
    public ResponseData guardarPlanTrabajo(PlanTrabajoJSON planTrabajoJSON) {

        List<String> errores = new ArrayList<>();
        var bimestre = userFacade.getUserSession().getBimestre();
        List<MateriaDescripcionId> materias = materiaRepository.findByIdGradoId(planTrabajoJSON.getIdGrado());

        //recorrer materias para verificar que la suma de actividades sea igual a 60 (puntos configurados en bimestre)
        for (MateriaDescripcionId materia : materias) {

            List<ActividadJSON> actividades = planTrabajoJSON.getActividades()
                    .stream()
                    .filter(actividad -> Objects.equals(actividad.getIdMateria(), materia.getId()))
                    .collect(Collectors.toList());

            var sumaValorActividadesMateria = actividades.stream().mapToDouble(ActividadJSON::getValorActividad).sum();

            if (sumaValorActividadesMateria != bimestre.getPuntosActividades()) {
                errores.add("La suma de actividades para " + materia.getDescripcion() + " debe ser igual a " + String.format("%.2f", bimestre.getPuntosActividades()) + " puntos");
            }
        }

        // contar cuantas materias tiene el plan de trabajo (contar solo las no repetidas)
        var materiasPlanTrabajo = planTrabajoJSON.getActividades()
                .stream()
                .map(ActividadJSON::getIdMateria)
                .distinct()
                .count();
        log.info("countMaterias: " + materiasPlanTrabajo);

        // validar que el plan de trabajo tenga todas las materias
        if (materiasPlanTrabajo != materias.size()) {
            errores.add("El plan de trabajo debe tener todas las materias cargadas como actividades");
        }

        //validar que el plan de trabajo no esté registrado para el bimestre y grado
        if (planTrabajoRepository.existsByIdBimestreIdAndIdGradoIdAndEstado(bimestre.getId(), planTrabajoJSON.getIdGrado()) > 0) {
            log.info("plan de trabajo ya registrado");
            errores.add("Ya existe un plan de trabajo registrado para el bimestre y grado seleccionado");
        }


        //si hay errores retornar el arreglo de errores y el estado de la operacion como 0 (fallida)
        if (errores.size() > 0) {
            return ResponseData.builder()
                    .code(0)
                    .message("Error al guardar el plan de trabajo para el bimestre " + bimestre.getDescripcion() + " lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        log.info("guardando plan de trabajo...");

        var idUsuario = usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario());
        var idGrado = gradoRepository.getReferenceById(planTrabajoJSON.getIdGrado());


        //asignar valores del planTrabajoJSON a un objeto de tipo PlanTrabajo
        var planTrabajo = new PlanTrabajo();
        planTrabajo.setDescripcion(planTrabajoJSON.getDescripcion());
        planTrabajo.setIdBimestre(bimestre);
        planTrabajo.setIdGrado(idGrado);
        planTrabajo.setEstado(PlanTrabajo.ACTIVO);
        planTrabajo.setFechaCommit(LocalDate.now());
        planTrabajo.setHoraCommit(LocalTime.now());
        planTrabajo.setIdUsuario(idUsuario);

        //guardar el planTrabajo
        planTrabajoRepository.save(planTrabajo);

        registrarActividadesNuevas(planTrabajoJSON.getActividades(), planTrabajo, idUsuario);

        log.info("plan de trabajo guardado");


        return ResponseData.builder()
                .message("Registrados exitosamente")
                .code(1)
                .data(planTrabajoJSON)
                .build();
    }

    @Transactional
    @Override
    public ResponseData actualizarActividadesPlanTrabajo(long idPlan, List<ActividadJSON> actividadesJson) {

        var planTrabajo = planTrabajoRepository.findById(idPlan).orElseThrow(() -> new RuntimeException("No existe el plan de trabajo"));
        List<String> errores = new ArrayList<>();
        var bimestre = userFacade.getUserSession().getBimestre();
        List<MateriaDescripcionId> materias = materiaRepository.findByIdGradoId(planTrabajo.getIdGrado().getId());

        //recorrer materias para verificar que la suma de actividades sea igual a 60 (puntos configurados en bimestre)
        for (MateriaDescripcionId materia : materias) {

            List<ActividadJSON> actividades = actividadesJson
                    .stream()
                    .filter(actividad -> Objects.equals(actividad.getIdMateria(), materia.getId()))
                    .filter(actividad -> actividad.getEstado() == Actividad.ACTIVO)// solo con estado activo
                    .collect(Collectors.toList());

            var sumaValorActividadesMateria = actividades.stream().mapToDouble(ActividadJSON::getValorActividad).sum();

            if (sumaValorActividadesMateria != bimestre.getPuntosActividades()) {
                errores.add("La suma de actividades para " + materia.getDescripcion() + " debe ser igual a " + String.format("%.2f", bimestre.getPuntosActividades()) + " puntos");
            }
        }

        // contar cuantas materias tiene el plan de trabajo (contar solo las no repetidas)
        var materiasPlanTrabajo = actividadesJson
                .stream()
                .filter(actividad -> actividad.getEstado() == Actividad.ACTIVO)// solo con estado activo
                .map(ActividadJSON::getIdMateria)
                .distinct()
                .count();

        log.info("countMaterias: " + materiasPlanTrabajo);

        // validar que el plan de trabajo tenga todas las materias
        if (materiasPlanTrabajo != materias.size()) {
            errores.add("El plan de trabajo debe tener todas las materias cargadas como actividades");
        }

        //si hay errores retornar el arreglo de errores y el estado de la operacion como 0 (fallida)
        if (errores.size() > 0) {
            return ResponseData.builder()
                    .code(0)
                    .message("Error al guardar el plan de trabajo para el bimestre " + bimestre.getDescripcion() + " lea los siguientes errores:")
                    .errors(errores)
                    .build();
        }

        log.info("actualizando actividades plan de trabajo...");

        List<ActividadJSON> actividadesActualizar = actividadesJson.stream()
                .filter(actividad -> actividad.getId() != null)
                .collect(Collectors.toList());

        List<ActividadJSON> actividadesNuevas = actividadesJson.stream()
                .filter(actividad -> actividad.getId() == null)
                .collect(Collectors.toList());

        var idUsuario = usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario());

        //registrar las que tengan id null
        registrarActividadesNuevas(actividadesNuevas, planTrabajo, idUsuario);

        //actualizar las que tengan id
        for (ActividadJSON actividadJSON : actividadesActualizar) {
            actividadRepository.actualizarActividad(actividadJSON.getId(), actividadJSON.getEstado(), actividadJSON.getValorActividad());
        }

        return ResponseData.builder()
                .message("Actualizado exitosamente")
                .code(1)
                .build();

    }

    private void registrarActividadesNuevas(List<ActividadJSON> actividadesNuevas, PlanTrabajo planTrabajo, Usuario idUsuario) {
        List<Actividad> actividades = new ArrayList<>();
        for (var actividad : actividadesNuevas) {
            var actividadGuardar = new Actividad();
            actividadGuardar.setDescripcionActividad(actividad.getDescripcionActividad());
            actividadGuardar.setValorActividad(actividad.getValorActividad());
            actividadGuardar.setIdMateria(materiaRepository.getReferenceById(actividad.getIdMateria()));
            actividadGuardar.setIdPlanTrabajo(planTrabajo);
            actividadGuardar.setIdUsuario(idUsuario);
            actividadGuardar.setEstado(Actividad.ACTIVO);
            actividades.add(actividadGuardar);
        }
        actividadRepository.saveAll(actividades);
    }
}
