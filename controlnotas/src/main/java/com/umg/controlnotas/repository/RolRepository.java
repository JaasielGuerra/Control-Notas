package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Long> {

    List<Rol> findByEstado(Integer estado);
}