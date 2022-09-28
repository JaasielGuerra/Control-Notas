package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.CicloEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CicloEscolarRepository extends JpaRepository<CicloEscolar, Long> {
    CicloEscolar findByEstadoAndAnio(int estado, int anio);
    List<CicloEscolar> findCicloEscolarsByEstadoInAndAnio(Collection<Integer> estados, int anio);

    List<CicloEscolar> findCicloEscolarsByEstadoInOrderByIdDesc(List<Integer> estados);
    @Modifying
    @Query("update CicloEscolar c set c.estado = ?1 where c.id = ?2")
    void updateEstado(int estado, Long id);
}