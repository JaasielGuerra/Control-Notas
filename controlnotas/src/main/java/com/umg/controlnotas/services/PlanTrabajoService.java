package com.umg.controlnotas.services;


import com.umg.controlnotas.model.dto.ActividadDto;
import com.umg.controlnotas.model.query.MateriaDescripcionId;
import com.umg.controlnotas.model.dto.PlanTrabajoDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlanTrabajoService {
    List<MateriaDescripcionId> obtenerMateriasPorGrado(Long id_grado, Long id_seccion);

    ResponseDataDto guardarPlanTrabajo(PlanTrabajoDto planTrabajoDto);

    @Transactional
    ResponseDataDto actualizarActividadesPlanTrabajo(long idPlan, List<ActividadDto> actividades);

    @Transactional
    ResponseDataDto eliminarPlanTrabajo(long idPlan);

    ResponseDataDto validarActividades(List<ActividadDto> actividades);
}
