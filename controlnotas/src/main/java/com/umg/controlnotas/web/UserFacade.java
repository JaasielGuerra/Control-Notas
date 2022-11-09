package com.umg.controlnotas.web;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.dto.AsignacionUsuarioDto;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fachada para recuperar al usuario en sesion
 */
@Component
public class UserFacade {

    public UserSession getUserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserSession) authentication.getPrincipal();
    }

    public CicloEscolar getCicloActual(){
        return getUserSession().getCicloEscolar();
    }

    public Bimestre getBimestreActual(){
        return getUserSession().getBimestre();
    }

    public void refreshBimestre(Bimestre bimestre){
        UserSession userSession = getUserSession();
        userSession.setBimestre(bimestre);
    }

    public void refreshCicloEscolar(CicloEscolar cicloEscolar){
        UserSession userSession = getUserSession();
        userSession.setCicloEscolar(cicloEscolar);
    }

    public List<AsignacionUsuarioDto> getAsignacionesUsuario(){
        return getUserSession().getAsignacionesUsuario();
    }

    public List<Long> obtenerIdsSecciones(Long idGrado){
        return getUserSession().getAsignacionesUsuario().stream()
                .filter(asignacionUsuarioDto -> asignacionUsuarioDto.getIdGrado().equals(idGrado))
                .map(AsignacionUsuarioDto::getIdSeccion)
                .collect(Collectors.toList());
    }


    public List<Long> obtenerTodosIdsSecciones(){
        return getUserSession().getAsignacionesUsuario()
                .stream()
                .map(AsignacionUsuarioDto::getIdSeccion)
                .collect(Collectors.toList());
    }
}
