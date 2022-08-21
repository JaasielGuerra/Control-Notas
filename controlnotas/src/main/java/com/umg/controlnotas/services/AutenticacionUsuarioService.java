package com.umg.controlnotas.services;

import com.umg.controlnotas.model.custom.UserSession;
import com.umg.controlnotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Implementacion personalizada de UserDetailsService de spring security
 */
@Service("userDetailsService")
public class AutenticacionUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * obtener usuario por nombre para autenticar con spring security. Este método es transaccional en modo de lectura
     *
     * @param username el nombre de usuario
     * @return usaerDetails
     * @throws UsernameNotFoundException si usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        var usuario = usuarioRepository.findUsuarioByUser(username);

        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        var rol = usuario.getIdRol();
        var roles = new ArrayList<GrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(rol.getDescripcion()));

        //devolver el objeto User que necesita spring security
        return new UserSession(usuario.getUser(), usuario.getPassword(), roles, usuario.getId(), usuario.getNombreCompleto());
    }
}