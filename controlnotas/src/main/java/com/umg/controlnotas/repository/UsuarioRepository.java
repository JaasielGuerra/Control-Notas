package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Usuario;
import com.umg.controlnotas.model.query.ConsultaUsuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByUserAndEstado(String username, Integer estado);
    List<ConsultaUsuarios> findByOrderByIdDesc();
    @Modifying
    @Query("update Usuario u set u.estado = ?1 where u.id = ?2")
    void updateEstadoUsuario(Integer estado, Long id);
}