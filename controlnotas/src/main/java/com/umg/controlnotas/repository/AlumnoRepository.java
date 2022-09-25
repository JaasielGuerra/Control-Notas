package com.umg.controlnotas.repository;

import com.umg.controlnotas.model.Alumno;
import com.umg.controlnotas.model.Seccion;
import com.umg.controlnotas.model.dto.AlumnoLecturaDto;
import com.umg.controlnotas.model.query.*;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query("SELECT " +
            "a.id AS id, a.codigoAlumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacionExpediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estadoExpediente AS estadoExpediente " +
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
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estadoExpediente AS estadoExpediente " +
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
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estadoExpediente AS estadoExpediente " +
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
    public void eliminar(Long id);

    @Query("SELECT a.idSeccion.id FROM Alumno  a WHERE a.id = ?1")
    Long obtenerIdSeccion(long id);


    @Modifying
    @Query("UPDATE " +
            "Alumno a " +
            "SET a.idSeccion = ?1 " +
            "WHERE " +
            "a.id = ?2")
    public void updateSecccion(Seccion seccion, Long id);

    @Query(value = "SELECT func_contar_alumnos_expediente_incompleto()", nativeQuery = true)
    int contarAlumnosExpedienteIncompleto();

    @Query("SELECT a.id AS idAlumno, " +
            "a.estadoExpediente AS estadoExpediente, " +
            "a.observacionExpediente AS observacionExpediente " +
            "FROM Alumno a " +
            "WHERE a.id = ?1")
    DatosExpediente findByIdAlumno(Long id);

    @Modifying
    @Query("UPDATE " +
            "Alumno a " +
            "SET a.estadoExpediente = ?1 ," +
            "a.observacionExpediente = ?2 " +
            "WHERE " +
            "a.id = ?3")
    public void updateDatosExpediente(Integer estadoExpediente, String observacion, Long id);

    List<ConsultaAlumnoCalificacion> findByEstadoAndIdSeccionIdIn(int estado, List<Long> secciones);

    @Query(value="CALL proc_listar_alumnos_lectura(?1, ?2)", nativeQuery = true)
    List<ConsultaAlumnosLectura> listarAlumnosLectura(Integer estado, Long idGrado);

    @Query(value = "SELECT " +
            "a.id_alumno AS idAlumno, " +
            "a.nombre AS nombre, " +
            "a.apellido AS apellido, " +
            "COALESCE(func_consultar_libro_actual_alumno(?1), 'NINGÚN LIBRO') AS libroActual, " +
            "COALESCE(func_alumno_tiene_libro_asignado(?1), 0) AS tieneLibro, " +
            "func_obtener_id_ultimo_libro_alumno(?1) AS idLibro " +
            "FROM alumno a " +
            "WHERE a.id_alumno = ?1", nativeQuery = true)
    ConsultaAlumnosLectura obtenerAlumnoLectura(Long idAlumno);

}