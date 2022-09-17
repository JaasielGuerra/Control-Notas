package com.umg.controlnotas.web;

import com.umg.controlnotas.model.AsignacionMateria;
import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.dto.AsignacionUsuarioDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserSession extends User {

    private final Long idUsuario;
    private final String nombreCompleto;
    private Bimestre bimestre;
    private CicloEscolar cicloEscolar;
    private final List<AsignacionUsuarioDto> asignacionesUsuario;

    public UserSession(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       Long idUsuario, String nombreCompleto,
                       Bimestre bimestre, CicloEscolar cicloEscolar,
                       List<AsignacionUsuarioDto> asignacionesUsuario) {
        super(username, password, authorities);
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.bimestre = bimestre;
        this.cicloEscolar = cicloEscolar;
        this.asignacionesUsuario = asignacionesUsuario;
    }
}
