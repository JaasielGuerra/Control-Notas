-- liquibase formatted sql

-- changeset liquibase:jaasiel-1 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_porcentaje_asistencia_alumno;

-- changeset liquibase:jaasiel-2 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION db_control_notas.func_obtener_porcentaje_asistencia_alumno(
    id_ciclo_actual long,
    id_bimestre_actual long,
    id_alumno long
)
    RETURNS DOUBLE(16,2)
BEGIN

    -- Variables
    DECLARE dias_base INTEGER;
    DECLARE inasistencias INTEGER;

    -- Obtener los dias base
    SELECT
        ce.dias_base_asistencia INTO dias_base
    FROM
        ciclo_escolar ce
    WHERE
            ce.id_ciclo_escolar = id_ciclo_actual;

    -- Obtener las inasistencias del alumno
    SELECT
        COUNT(dl.id_detalle_listado) INTO inasistencias
    FROM
        detalle_listado dl
            JOIN listado_asistencia la ON
                la.id_listado_asistencia = dl.id_listado_asistencia
    WHERE
            dl.id_alumno = id_alumno
      AND
            dl.id_bimestre = id_bimestre_actual	-- El bimestre actual
      AND
            la.estado = 1	-- que el listado esté activo
      AND
            la.tipo = 1	-- solo tipo ASISTENCIA
      AND
      -- FALTÓ
            dl.motivo = 'F';


    RETURN (

        -- Formula calculo % asistencia: (DIAS_BASE - INASISTENCIAS ) / DIAS_BASE
        SELECT ((dias_base - inasistencias) / dias_base) * 100

    );

END