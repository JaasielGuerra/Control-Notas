package com.umg.controlnotas.model.custom;

import com.umg.controlnotas.model.Bimestre;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserSession extends User {

    private final Long idUsuario;
    private final String nombreCompleto;
    private final Bimestre bimestre;

    public UserSession(String username, String password, Collection<? extends GrantedAuthority> authorities, Long idUsuario, String nombreCompleto, Bimestre bimestre) {
        super(username, password, authorities);
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.bimestre = bimestre;
    }
}
