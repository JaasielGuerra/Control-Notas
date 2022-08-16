package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.custom.AlumnoConsultar;
import com.umg.controlnotas.model.custom.AlumnoEditar;
import com.umg.controlnotas.model.custom.AlumnoJSON;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query("SELECT " +
            "a.id AS id, a.codigoAlumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacionExpediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion " +
            "FROM " +
            "Alumno a " +
            "LEFT JOIN " +
            "Seccion s ON s = a.idSeccion " +
            "LEFT JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "a.estado = 1 " +
            "AND " +
            "a.idSeccion.id = ?1")
    public List<AlumnoConsultar> findAlumnosActivosBySeccion(Long seccion);

    @Query("SELECT " +
            "a.id AS id, a.codigoAlumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacionExpediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion " +
            "FROM " +
            "Alumno a " +
            "LEFT JOIN " +
            "Seccion s ON s = a.idSeccion " +
            "LEFT JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "a.estado = 1 " +
            "AND " +
            "a.idSeccion.id IS NULL")
    public List<AlumnoConsultar> findAlumnosActivosNoAsignados();

    @Query("SELECT " +
            "a.id AS id, a.codigoAlumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacionExpediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion " +
            "FROM " +
            "Alumno a " +
            "LEFT JOIN " +
            "Seccion s ON s = a.idSeccion " +
            "LEFT JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "a.estado = 1 ")
    public List<AlumnoConsultar> findAlumnosActivos();

    @Query("SELECT " +
            "a.id AS id, a.codigoAlumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.direccion AS direccion, a.fechaNacimiento AS nacimiento," +
            "g.id AS grado, s.id AS seccion " +
            "FROM " +
            "Alumno a " +
            "LEFT JOIN " +
            "Seccion s ON s = a.idSeccion " +
            "LEFT JOIN " +
            "Grado g ON g = s.idGrado " +
            "WHERE " +
            "a.id = ?1 ")
    public AlumnoEditar findAlumno(Long id);

    @Modifying
    @Query("UPDATE " +
            "Alumno a " +
            "SET a.estado = 0 " +
            "WHERE " +
            "a.id = ?1")
    public  void eliminar(Long id);
}