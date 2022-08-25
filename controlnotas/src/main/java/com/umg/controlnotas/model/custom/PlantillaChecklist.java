package com.umg.controlnotas.model.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantillaChecklist {

    public PlantillaChecklist() {
    }

    private Long idDocumentoExpediente;
    private Integer estado;
    private Long idDetalleExpediente;
    private String descripcionDocumento;
}
