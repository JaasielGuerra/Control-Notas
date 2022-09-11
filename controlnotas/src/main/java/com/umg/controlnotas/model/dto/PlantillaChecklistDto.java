package com.umg.controlnotas.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlantillaChecklistDto implements Serializable {

    public PlantillaChecklistDto() {
    }

    private Long idDocumentoExpediente;
    private Integer estado;
    private Long idDetalleExpediente;
    private String descripcionDocumento;
}
