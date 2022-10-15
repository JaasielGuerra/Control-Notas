package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.query.ConsultaBimestresCiclo;
import com.umg.controlnotas.model.query.RubricaUltimoBimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BimestreRepository extends JpaRepository<Bimestre, Long> {
    Bimestre findByEstado(int estado);
    RubricaUltimoBimestre findTopByOrderByIdDesc();
    boolean existsByEstado(int estado);


    List<ConsultaBimestresCiclo> findBimestresByEstadoInOrderByIdDesc(Collection<Integer> estados);
    @Query("SELECT b.id FROM Bimestre b WHERE b.idCicloEscolar.id = ?1 AND NOT b.estado = ?2 ORDER BY b.id ASC")
    List<Long> findIdBimestreByIdCicloEscolarId(Long idCicloEscolar, int estado);
}