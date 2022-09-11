package com.umg.controlnotas.web;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Fachada para recuperar al usuario en sesion
 */
@Component
@Getter
public class UserFacade {

    public UserSession getUserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserSession) authentication.getPrincipal();
    }
}
