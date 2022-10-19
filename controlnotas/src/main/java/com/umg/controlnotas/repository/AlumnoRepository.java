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

    @Query(value = "SELECT " +
            "a.id_alumno AS id, a.codigo_alumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacion_expediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estado_expediente AS estadoExpediente, " +
            "func_obtener_porcentaje_asistencia_alumno(?2, ?3, a.id_alumno) AS porcentajeAsistencia " +
            "FROM " +
            "alumno a " +
            "LEFT JOIN " +
            "seccion s ON s.id_seccion = a.id_seccion " +
            "LEFT JOIN " +
            "grado g ON g.id_grado = s.id_grado " +
            "WHERE " +
            "a.estado = 1 " +
            "AND " +
            "a.id_seccion = ?1", nativeQuery = true)
    public List<AlumnoConsultar> findAlumnosActivosBySeccion(Long seccion, Long idCicloActual, Long idBimestreActual);

    @Query("SELECT " +
            "a.id AS id " +
            "FROM " +
            "Alumno a " +
            "WHERE " +
            "a.estado = 1 " +
            "AND " +
            "a.idSeccion.id = ?1")
    List<Long> findIdAlumnosActivosBySeccion(Long seccion);

    @Query(value = "SELECT " +
            "a.id_alumno AS id, a.codigo_alumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacion_expediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estado_expediente AS estadoExpediente, " +
            "func_obtener_porcentaje_asistencia_alumno(?1, ?2, a.id_alumno) AS porcentajeAsistencia " +
            "FROM " +
            "alumno a " +
            "LEFT JOIN " +
            "seccion s ON s.id_seccion = a.id_seccion " +
            "LEFT JOIN " +
            "grado g ON g.id_grado = s.id_grado " +
            "WHERE " +
            "a.estado = 1 " +
            "AND " +
            "a.id_seccion IS NULL", nativeQuery = true)
    public List<AlumnoConsultar> findAlumnosActivosNoAsignados(Long idCicloActual, Long idBimestreActual);

    @Query(value = "SELECT " +
            "a.id_alumno AS id, a.codigo_alumno AS codigo,a.nombre AS nombre,a.apellido AS apellido," +
            "a.observacion_expediente AS observacionExpediente," +
            "CONCAT(g.descripcion, ' ', s.descripcion) AS descripcionGradoSeccion," +
            "a.estado_expediente AS estadoExpediente, " +
            "func_obtener_porcentaje_asistencia_alumno(?1, ?2, a.id_alumno) AS porcentajeAsistencia " +
            "FROM " +
            "alumno a " +
            "LEFT JOIN " +
            "seccion s ON s.id_seccion = a.id_seccion " +
            "LEFT JOIN " +
            "grado g ON g.id_grado = s.id_grado " +
            "WHERE " +
            "a.estado = 1 ", nativeQuery = true)
    public List<AlumnoConsultar> findAlumnosActivos(Long idCicloActual, Long idBimestreActual);

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

    @Query(value="CALL proc_reporte_alumnos(?1, ?2, ?3)", nativeQuery = true)
    List<AlumnoReporte> consultarReporteAlumnos(Long seccion, Long idCicloActual, Long idBimestreActual);

    @Query(value="CALL proc_datos_alumno(?1, ?2)", nativeQuery = true)
    DatosAlumnoReporte consultarDatosAlumno(String codAlumno, Long idCiclo);

    @Query(value="CALL proc_reporte_notas_por_bimestre(?1, ?2)", nativeQuery = true)
    List<ConsultaReporteNotasBimestre> consultarReporteNotasBimestre(Long idBimestre, Long idAlumno);

    @Query(value="CALL proc_reporte_notas_finales(?1,?2, ?3, ?4, ?5)", nativeQuery = true)
    List<ConsultaReporteNotaFinal> consultarReporteNotasFinales(Long idBimestre1, Long idBimestre2, Long idBimestre3, Long idBimestre4, Long idAlumno);

    @Query(value="CALL proc_reporte_actitudinal_alumno(?1, ?2)", nativeQuery = true)
    List<ConsultaReporteActitudinalAlumno> consultarReporteActitudinalAlumno(Long idBimestre, Long idAlumno);
}