package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.query.ConsultaUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByUser(String username);
    List<ConsultaUsuarios> findByOrderByIdDesc();
}