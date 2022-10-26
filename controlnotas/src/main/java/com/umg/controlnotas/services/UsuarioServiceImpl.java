package com.umg.controlnotas.services;

import com.umg.controlnotas.model.AsignacionMateria;
import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.dto.AsignacionMateriaDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.dto.UsuarioDto;
import com.umg.controlnotas.model.query.AsignacionUsuario;
import com.umg.controlnotas.model.query.ConsultaUsuarios;
import com.umg.controlnotas.repository.*;
import com.umg.controlnotas.util.EncriptarPassword;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private AsignacionMateriaRepository asignacionMateriaRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private SeccionRepository seccionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Registra un nuevo usuario
     *
     * @param usuarioDto usuario a registrar
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto registrarUsuario(UsuarioDto usuarioDto) {

        //validar que el usuario no exista
        boolean existsByUser = usuarioRepository.existsByUser(usuarioDto.getUser());

        if (existsByUser) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("El nombre de usuario <strong>" + usuarioDto.getUser() + "</strong> ya existe, por favor ingrese otro")
                    .build();
        }

        //validar que el usuario tenga asignaciones materia
        if (usuarioDto.getAsignaciones().isEmpty()) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("El usuario debe tener asignaciones de materias")
                    .build();
        }

        //validar que las asignaciones materia no se repitan
        List<AsignacionMateriaDto> asignacionesRepetidas = usuarioDto.getAsignaciones()
                .stream()
                //agrupar por id de materia y seccion
                .collect(Collectors.groupingBy(item -> item.getIdMateria() + "-" + item.getIdSeccion()))
                .values()
                .stream()
                //filtrar los grupos que tengan mas de un elemento
                .filter(item -> item.size() > 1)
                .map(item -> item.get(0))
                .collect(Collectors.toList());

        if (!asignacionesRepetidas.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("No se puede registrar el usuario, existen asignaciones de materias repetidas")
                    .errors(asignacionesRepetidas.stream().map(item -> item.getGradoSeccion() + " - " + item.getMateria()).collect(Collectors.toList()))
                    .build();
        }


        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(usuarioDto.getNombreCompleto());
        usuario.setUser(usuarioDto.getUser());
        usuario.setPassword(EncriptarPassword.encriptar(usuarioDto.getPassword()));
        usuario.setEstado(Usuario.ESTADO_ACTIVO);
        usuario.setFechaCommit(LocalDate.now());
        usuario.setHoraCommit(LocalTime.now());
        usuario.setIdRol(rolRepository.getReferenceById(usuarioDto.getIdRol()));
        usuarioRepository.save(usuario);

        //registra las asignaciones de materias
        List<AsignacionMateria> asignaciones = usuarioDto.getAsignaciones().stream()
                .map(item -> {
                    AsignacionMateria asignacionMateria = new AsignacionMateria();
                    asignacionMateria.setIdUsuario(usuario);
                    asignacionMateria.setIdMateria(materiaRepository.getReferenceById(item.getIdMateria()));
                    asignacionMateria.setEstado(AsignacionMateria.ACTIVO);
                    asignacionMateria.setIdSeccion(seccionRepository.getReferenceById(item.getIdSeccion()));
                    return asignacionMateria;
                })
                .collect(Collectors.toList());

        asignacionMateriaRepository.saveAll(asignaciones);

        return ResponseDataDto.builder()
                .code(ResponseDataDto.SUCCESS)
                .message("Usuario registrado correctamente")
                .build();
    }


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
     *
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
     *
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

    /**
     * Obttener alumno editar por id
     *
     * @param id identificador del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuarioEditar(Long id) {

        log.info("obteniendo usuario con id: " + id);

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el usuario con id: " + id));
        List<AsignacionUsuario> asignaciones = asignacionMateriaRepository.findByIdUsuarioIdAndEstado(usuario.getId(), AsignacionMateria.ACTIVO);

        log.info("asignaciones: " + asignaciones);

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .user(usuario.getUser())
                .password(usuario.getPassword())
                .idRol(usuario.getIdRol().getId())
                .estado(usuario.getEstado())
                .asignaciones(asignaciones.stream().map(item -> AsignacionMateriaDto.builder()
                        .idMateria(item.getIdMateriaId())
                        .idSeccion(item.getIdSeccionId())
                        .gradoSeccion(item.getIdSeccionIdGradoDescripcion() + " " + item.getIdSeccionDescripcion())
                        .materia(item.getIdMateriaDescripcion())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    /**
     * Actualizar usuario
     *
     * @param id
     * @param usuarioDto
     * @return
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarUsuario(Long id, UsuarioDto usuarioDto) {

        //validar que el usuario no exista (descartando el mismo usuario)
        boolean existsByUser = usuarioRepository.existsByUserAndIdNot(usuarioDto.getUser(), id);

        if (existsByUser) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("El nombre de usuario <strong>" + usuarioDto.getUser() + "</strong> ya existe, por favor ingrese otro")
                    .build();
        }

        //validar que el usuario tenga asignaciones materia
        if (usuarioDto.getAsignaciones().isEmpty()) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("El usuario debe tener asignaciones de materias")
                    .build();
        }

        //validar que las asignaciones materia no se repitan
        List<AsignacionMateriaDto> asignacionesRepetidas = usuarioDto.getAsignaciones()
                .stream()
                //agrupar por id de materia y seccion
                .collect(Collectors.groupingBy(item -> item.getIdMateria() + "-" + item.getIdSeccion()))
                .values()
                .stream()
                //filtrar los grupos que tengan mas de un elemento
                .filter(item -> item.size() > 1)
                .map(item -> item.get(0))
                .collect(Collectors.toList());

        if (!asignacionesRepetidas.isEmpty()) {
            return ResponseDataDto.builder()
                    .code(ResponseDataDto.ERROR)
                    .message("No se puede registrar el usuario, existen asignaciones de materias repetidas")
                    .errors(asignacionesRepetidas.stream().map(item -> item.getGradoSeccion() + " - " + item.getMateria()).collect(Collectors.toList()))
                    .build();
        }

        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el usuario con id: " + id));
        usuario.setNombreCompleto(usuarioDto.getNombreCompleto());
        usuario.setUser(usuarioDto.getUser());
        usuario.setIdRol(rolRepository.getReferenceById(usuarioDto.getIdRol()));
        usuarioRepository.save(usuario);

        //actualiza las asignaciones de materias
        List<AsignacionMateria> asignaciones = usuarioDto.getAsignaciones().stream()
                .map(item -> {
                    AsignacionMateria asignacionMateria = new AsignacionMateria();
                    asignacionMateria.setIdUsuario(usuario);
                    asignacionMateria.setIdMateria(materiaRepository.getReferenceById(item.getIdMateria()));
                    asignacionMateria.setEstado(AsignacionMateria.ACTIVO);
                    asignacionMateria.setIdSeccion(seccionRepository.getReferenceById(item.getIdSeccion()));
                    return asignacionMateria;
                })
                .collect(Collectors.toList());

        //eliminar las asignaciones de materias
        asignacionMateriaRepository.updateEstadoAsignacionMateriaByIdUsuario(id, AsignacionMateria.INACTIVO);

        //guardar las nuevas asignaciones de materias
        asignacionMateriaRepository.saveAll(asignaciones);

        return ResponseDataDto.builder()
                .code(ResponseDataDto.SUCCESS)
                .message("Usuario actualizado correctamente")
                .data(usuarioDto)
                .build();
    }

    /**
     * Actualizar contraseña de usuario
     *
     * @param id
     * @param usuarioDto
     * @return
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarContrasenia(Long id, UsuarioDto usuarioDto) {

        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No existe el usuario con id: " + id));
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        usuarioRepository.save(usuario);

        return ResponseDataDto.builder()
                .code(ResponseDataDto.SUCCESS)
                .message("Contraseña actualizada correctamente")
                .data(usuarioDto)
                .build();
    }

}
