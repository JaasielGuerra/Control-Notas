package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Bimestre;
import com.umg.controlnotas.model.CicloEscolar;
import com.umg.controlnotas.model.query.ConsultaBimestresCiclo;
import com.umg.controlnotas.model.query.RubricaUltimoBimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BimestreRepository extends JpaRepository<Bimestre, Long> {
    Bimestre findByEstado(int estado);
    Bimestre findByEstadoAndIdCicloEscolar(int estado, CicloEscolar idCicloEscolar);
    RubricaUltimoBimestre findTopByOrderByIdDesc();
    boolean existsByEstado(int estado);


    List<ConsultaBimestresCiclo> findBimestresByEstadoInOrderByIdDesc(Collection<Integer> estados);
    @Query("SELECT b.id FROM Bimestre b WHERE b.idCicloEscolar.id = ?1 AND NOT b.estado = ?2 ORDER BY b.id ASC")
    List<Long> findIdBimestreByIdCicloEscolarId(Long idCicloEscolar, int estado);

    List<ConsultaBimestresCiclo> findBimestresByIdCicloEscolarIdAndEstadoNot(Long idCicloEscolar, int estado);
    int countByIdCicloEscolarIdAndEstadoNot(Long idCicloEscolar, int estado);
    @Modifying
    @Query("UPDATE Bimestre b SET b.estado = ?1 WHERE b.estado = ?2")
    void updateEstadoByEstado(int estadoNuevo, int estadoAnterior);
}