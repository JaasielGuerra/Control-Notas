package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.UsuarioDto;
import com.umg.controlnotas.model.query.ConsultaUsuarios;
import com.umg.controlnotas.repository.UsuarioRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
