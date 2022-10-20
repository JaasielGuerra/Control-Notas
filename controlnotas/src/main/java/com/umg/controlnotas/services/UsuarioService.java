package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDto> consultarUsuarios();
}
