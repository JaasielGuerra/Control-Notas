package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.ControlActitudinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ControlActitudinalRepository extends JpaRepository<ControlActitudinal, Long> {
    boolean existsByIdMateriaIdAndIdAlumnoIdAndIdBimestreId(Long idMateria, Long idAlumno, Long idBimestre);
    @Query(value = "SELECT func_obtener_puntos_conducta_alumno(:idBimestre,:idAlumno,:idMateria);", nativeQuery = true)
    double obtenerPuntosConducta(@Param("idBimestre") Long idBimestre, @Param("idAlumno") Long idAlumno, @Param("idMateria") Long idMateria);
}