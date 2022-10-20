package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
import com.umg.controlnotas.model.query.ConsultaUsuarios;
import com.umg.controlnotas.repository.UsuarioRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    /**
     * Consultar a los usuarios registrados
     *
     * @return
     */
    @Override
    public List<UsuarioDto> consultarUsuarios() {

        log.info("consultando todos los usuarios");

        List<ConsultaUsuarios> usuarios = usuarioRepository.findByOrderByIdDesc();

        return usuarios.stream().map(usuario -> UsuarioDto.builder()
                        .id(usuario.getId())
                        .nombreCompleto(usuario.getNombreCompleto())
                        .user(usuario.getUser())
                        .idRolDescripcion(usuario.getIdRolDescripcion())
                        .estado(usuario.getEstado())
                        .build()).
                collect(java.util.stream.Collectors.toList());
    }

    /**
     * Desactivar usuario
     * @param id identificador del usuario
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto desactivarUsuario(Long id) {

        log.info("desactivando usuario con id: " + id);

        usuarioRepository.updateEstadoUsuario(Usuario.ESTADO_INACTIVO, id);
        return ResponseDataDto.builder()
                .message("Usuario desactivado correctamente!")
                .code(ResponseDataDto.SUCCESS)
                .build();
    }

    /**
     * Activar usuario
     * @param id identificador del usuario
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto activarUsuario(Long id) {

        log.info("activando usuario con id: " + id);

        usuarioRepository.updateEstadoUsuario(Usuario.ESTADO_ACTIVO, id);
        return ResponseDataDto.builder()
                .message("Usuario activado correctamente!")
                .code(ResponseDataDto.SUCCESS)
                .build();
    }
}
