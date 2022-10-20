package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDto> consultarUsuarios();

    ResponseDataDto desactivarUsuario(Long id);

    @Transactional
    ResponseDataDto activarUsuario(Long id);
}
