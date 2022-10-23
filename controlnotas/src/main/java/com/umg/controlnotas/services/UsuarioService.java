package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UsuarioService {
    @Transactional
    ResponseDataDto registrarUsuario(UsuarioDto usuarioDto);

    List<UsuarioDto> consultarUsuarios();

    ResponseDataDto desactivarUsuario(Long id);

    @Transactional
    ResponseDataDto activarUsuario(Long id);

    @Transactional (readOnly = true)
    UsuarioDto obtenerUsuarioEditar(Long id);

    ResponseDataDto actualizarUsuario(Long id, UsuarioDto usuarioDto);

    ResponseDataDto actualizarContrasenia(Long id, UsuarioDto usuarioDto);
}
