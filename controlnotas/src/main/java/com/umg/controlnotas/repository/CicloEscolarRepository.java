package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.CicloEscolar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CicloEscolarRepository extends JpaRepository<CicloEscolar, Long> {
    CicloEscolar findByEstadoAndAnio(int estado, int anio);
}