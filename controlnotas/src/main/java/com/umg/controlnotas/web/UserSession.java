package com.umg.controlnotas.web;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserSession extends User {

    private final Long idUsuario;
    private final String nombreCompleto;
    private Bimestre bimestre;
    private CicloEscolar cicloEscolar;

    public UserSession(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       Long idUsuario, String nombreCompleto,
                       Bimestre bimestre, CicloEscolar cicloEscolar) {
        super(username, password, authorities);
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.bimestre = bimestre;
        this.cicloEscolar = cicloEscolar;
    }
}
