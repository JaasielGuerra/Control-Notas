package com.umg.controlnotas.model.custom;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserSession extends User {

    private final Long idUsuario;
    private final String nombreCompleto;

    public UserSession(String username, String password, Collection<? extends GrantedAuthority> authorities, Long idUsuario, String nombreCompleto) {
        super(username, password, authorities);
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
    }
}
