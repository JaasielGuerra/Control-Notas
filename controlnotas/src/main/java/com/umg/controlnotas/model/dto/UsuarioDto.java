package com.umg.controlnotas.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UsuarioDto implements Serializable {

        private Long id;
        private String nombreCompleto;
        private String user;
        private String idRolDescripcion;
        private Integer estado;

}
