package com.umg.controlnotas.services;


import com.umg.controlnotas.model.custom.MateriaDescripcionId;
import com.umg.controlnotas.model.custom.PlanTrabajoJSON;
import com.umg.controlnotas.model.custom.ResponseData;

import java.util.List;

public interface PlanTrabajoService {
    List<MateriaDescripcionId> obtenerMateriasPorGrado(long id_grado);

    ResponseData guardarPlanTrabajo(PlanTrabajoJSON planTrabajoJSON);
}
