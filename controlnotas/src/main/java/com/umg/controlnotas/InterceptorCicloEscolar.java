/**
 * Interceptor para validar el ciclo escolar o bimestre abierto
 */

package com.umg.controlnotas;

import com.umg.controlnotas.services.CicloEscolarService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log
public class InterceptorCicloEscolar implements HandlerInterceptor {

    @Autowired
    private CicloEscolarService cicloEscolarService;

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //log output url request
        log.info("preHandle URL: " + request.getRequestURL());
        log.info("validando ciclo / bimestre apertado.. ");

        //obtener ciclo escolar actual aperturado
        var ciclo = cicloEscolarService.obtenerCicloActual();

        //si no existe ciclo escolar aperturado
        if (ciclo.getId() == null) {
            response.sendRedirect("/institucion/ciclo");
            return false;
        }

        //si no existe bimestre aperturado
        if (ciclo.getIdBimestreActual() == null) {
            log.info("no existe ciclo o bimestre aperturado, redireccionando a "
                    + request.getScheme()
                    + "://"
                    + request.getServerName()
                    + ":"
                    + request.getServerPort()
                    + request.getContextPath()
                    + "/institucion/ciclo");
            response.sendRedirect("/institucion/ciclo");
            return false;
        }

        log.info("ciclo escolar aperturado: " + ciclo);
        return true;
    }
}
