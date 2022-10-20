package com.umg.controlnotas.services;

import com.umg.controlnotas.model.AsignacionMateria;
import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.dto.AsignacionUsuarioDto;
import com.umg.controlnotas.repository.AsignacionMateriaRepository;
import com.umg.controlnotas.repository.CicloEscolarRepository;
import com.umg.controlnotas.web.UserSession;
import com.umg.controlnotas.repository.BimestreRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementacion personalizada de UserDetailsService de spring security
 */
@Service("userDetailsService")
public class AutenticacionUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BimestreRepository bimestreRepository;
    @Autowired
    private CicloEscolarRepository cicloEscolarRepository;
    @Autowired
    private AsignacionMateriaRepository asignacionMateriaRepository;

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


        var usuario = usuarioRepository.findUsuarioByUserAndEstado(username, Usuario.ESTADO_ACTIVO);

        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        var bimestre = bimestreRepository.findByEstado(Bimestre.ACTIVO);
        var cicloEscolar = cicloEscolarRepository.findByEstadoAndAnio(CicloEscolar.APERTURADO, LocalDate.now().getYear());
        var asignaturas = asignacionMateriaRepository.findByIdUsuarioIdAndEstado(usuario.getId(), AsignacionMateria.ACTIVO)
                .stream()
                .map(m -> {
                    var asignacionMateria = new AsignacionUsuarioDto();
                    asignacionMateria.setIdMateria(m.getIdMateriaId());
                    asignacionMateria.setDescripcionMateria(m.getIdMateriaDescripcion());
                    asignacionMateria.setDescripcionGrado(m.getIdMateriaIdGradoDescripcion());
                    return asignacionMateria;
                })
                .collect(Collectors.toList());

        var rol = usuario.getIdRol();
        var roles = new ArrayList<GrantedAuthority>();

        roles.add(new SimpleGrantedAuthority(rol.getDescripcion()));

        //devolver el objeto User que necesita spring security
        return new UserSession(usuario.getUser(), usuario.getPassword(), roles, usuario.getId(), usuario.getNombreCompleto(), bimestre, cicloEscolar, asignaturas);
    }
}
