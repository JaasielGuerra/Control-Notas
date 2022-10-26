package com.umg.controlnotas.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UsuarioDto implements Serializable {

        private Long id;
        private String nombreCompleto;
        private String user;
        private String idRolDescripcion;
        private Integer estado;
        private String password;
        private Long idRol;
        private List<AsignacionMateriaDto> asignaciones;

}
