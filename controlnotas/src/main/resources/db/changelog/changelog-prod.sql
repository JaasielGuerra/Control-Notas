-- liquibase formatted sql

-- changeset liquibase:jaasiel-1 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_porcentaje_asistencia_alumno;

-- changeset liquibase:jaasiel-2 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION db_control_notas.func_obtener_porcentaje_asistencia_alumno(
    id_ciclo_actual long,
    id_bimestre_actual long,
    id_alumno long
)
    RETURNS DOUBLE
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
        SELECT ((dias_base - inasistencias) / dias_base)

    );

END

-- changeset liquibase:jaasiel-3
ALTER TABLE db_control_notas.detalle_listado MODIFY COLUMN id_detalle_listado BIGINT auto_increment NOT NULL;


-- changeset liquibase:jaasiel-4
DROP TABLE `db_control_notas`.`historial_actitudinal`;

-- changeset liquibase:jaasiel-5
DROP TABLE `db_control_notas`.`control_actitudinal`;

-- changeset liquibase:jaasiel-6
CREATE TABLE IF NOT EXISTS `db_control_notas`.`control_actitudinal` (
                                                                        `id_control_actitudinal` BIGINT NOT NULL AUTO_INCREMENT,
                                                                        `id_materia` BIGINT NOT NULL,
                                                                        `id_alumno` BIGINT NOT NULL,
                                                                        `descripcion` VARCHAR(150) NOT NULL,
                                                                        `puntos_restados` DOUBLE(16 , 2 ) NOT NULL COMMENT 'cantidad de puntos que se agregan',
                                                                        `puntos_sumados` DOUBLE(16 , 2 ) NOT NULL COMMENT 'cantidad de puntos que se quitan',
                                                                        `puntos_actuales` DOUBLE(16 , 2 ) NOT NULL,
                                                                        `fecha` DATE NOT NULL,
                                                                        `fecha_commit` DATE NOT NULL,
                                                                        `hora_commit` TIME NOT NULL,
                                                                        `id_usuario` BIGINT NOT NULL,
                                                                        `id_bimestre` BIGINT NOT NULL,
                                                                        PRIMARY KEY (`id_control_actitudinal`),
                                                                        INDEX `fk_control_actitudinal_materia1_idx` (`id_materia` ASC),
                                                                        INDEX `fk_control_actitudinal_alumno1_idx` (`id_alumno` ASC),
                                                                        INDEX `fk_control_actitudinal_usuario1_idx` (`id_usuario` ASC),
                                                                        INDEX `fk_control_actitudinal_bimestre1_idx` (`id_bimestre` ASC),
                                                                        CONSTRAINT `fk_control_actitudinal_materia1` FOREIGN KEY (`id_materia`)
                                                                            REFERENCES `db_control_notas`.`materia` (`id_materia`)
                                                                            ON DELETE NO ACTION ON UPDATE NO ACTION,
                                                                        CONSTRAINT `fk_control_actitudinal_alumno1` FOREIGN KEY (`id_alumno`)
                                                                            REFERENCES `db_control_notas`.`alumno` (`id_alumno`)
                                                                            ON DELETE NO ACTION ON UPDATE NO ACTION,
                                                                        CONSTRAINT `fk_control_actitudinal_usuario1` FOREIGN KEY (`id_usuario`)
                                                                            REFERENCES `db_control_notas`.`usuario` (`id_usuario`)
                                                                            ON DELETE NO ACTION ON UPDATE NO ACTION,
                                                                        CONSTRAINT `fk_control_actitudinal_bimestre1` FOREIGN KEY (`id_bimestre`)
                                                                            REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
                                                                            ON DELETE NO ACTION ON UPDATE NO ACTION
)  ENGINE=INNODB;

-- changeset liquibase:jaasiel-7 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_alumnos;

-- changeset liquibase:jaasiel-8 endDelimiter:$$\nDELIMITER ;
CREATE  PROCEDURE `proc_reporte_alumnos`(
    seccion BIGINT,
    idCicloActual BIGINT,
    idBimestreActual BIGINT
)
BEGIN


    -- por seccion
    IF seccion IS NOT NULL AND seccion > 0 THEN
        SELECT
            a.codigo_alumno codigo,
            a.nombre nombre,
            a.apellido apellido,
            a.observacion_expediente observacion,
            a.estado_expediente expediente,
            func_obtener_porcentaje_asistencia_alumno(idCicloActual,
                                                      idBimestreActual,
                                                      a.id_alumno) asistencia,
            CONCAT(g.descripcion, ' ', s.descripcion) AS gradoSeccion
        FROM
            alumno a
                LEFT JOIN
            seccion s ON
                    s.id_seccion = a.id_seccion
                LEFT JOIN
            grado g ON
                    g.id_grado = s.id_grado
        WHERE
                a.estado = 1
          AND a.id_seccion = seccion;
    END IF;

    -- todos los alumnos
    IF seccion IS NULL THEN
        SELECT
            a.codigo_alumno codigo,
            a.nombre nombre,
            a.apellido apellido,
            a.observacion_expediente observacion,
            a.estado_expediente expediente,
            func_obtener_porcentaje_asistencia_alumno(idCicloActual,
                                                      idBimestreActual,
                                                      a.id_alumno) asistencia,
            CONCAT(g.descripcion, ' ', s.descripcion) AS gradoSeccion
        FROM
            alumno a
                LEFT JOIN
            seccion s ON
                    s.id_seccion = a.id_seccion
                LEFT JOIN
            grado g ON
                    g.id_grado = s.id_grado
        WHERE
                a.estado = 1;
    END IF;

    -- sin asignacion
    IF seccion = 0 THEN
        SELECT
            a.codigo_alumno codigo,
            a.nombre nombre,
            a.apellido apellido,
            a.observacion_expediente observacion,
            a.estado_expediente expediente,
            func_obtener_porcentaje_asistencia_alumno(idCicloActual,
                                                      idBimestreActual,
                                                      a.id_alumno) asistencia,
            CONCAT(g.descripcion, ' ', s.descripcion) AS gradoSeccion
        FROM
            alumno a
                LEFT JOIN
            seccion s ON
                    s.id_seccion = a.id_seccion
                LEFT JOIN
            grado g ON
                    g.id_grado = s.id_grado
        WHERE
                a.estado = 1
          AND a.id_seccion IS NULL;
    END IF;

END

-- changeset liquibase:jaasiel-9 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_datos_alumno;

-- changeset liquibase:jaasiel-10 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE proc_datos_alumno(
	codAlumno VARCHAR(45),
	idCiclo BIGINT
)
BEGIN
	SELECT
        a.id_alumno id,
		a.nombre nombre ,
		a.apellido apellido,
		a.codigo_alumno codigo,
		a.direccion direccion ,
		CONCAT(g.descripcion, ' ', s.descripcion) AS gradoSeccion,
		(
		SELECT
			ce.anio
		FROM
			ciclo_escolar ce
		WHERE
			ce.id_ciclo_escolar = idCiclo
		) anioCiclo
	FROM
		alumno a
	LEFT JOIN
	seccion s ON
		s.id_seccion = a.id_seccion
	LEFT JOIN
	grado g ON
		g.id_grado = s.id_grado
	WHERE
		a.codigo_alumno  = codAlumno
	    AND a.estado = 1
	LIMIT 1;

END

-- changeset liquibase:jaasiel-11 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_notas_por_bimestre;

-- changeset liquibase:jaasiel-12 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE `db_control_notas`.`proc_reporte_notas_por_bimestre`(
    idBimestre BIGINT,
    idAlumno BIGINT
)
BEGIN

    DECLARE puntosActitudinal DOUBLE(16,2);

    -- consultar los puntos actitudinal configurados en el bimestre
    SELECT b.puntos_actitudinal INTO puntosActitudinal FROM bimestre b WHERE b.id_bimestre = idBimestre;



    SELECT
        (
            SELECT
                b.descripcion
            FROM
                bimestre b
            WHERE
                    b.id_bimestre = idBimestre -- id bimestre
        ) bimestre,
        m.descripcion materia,
        COALESCE((
                     SELECT
                         SUM(dc.puntos_obtenidos)
                     FROM
                         detalle_calificacion dc
                             JOIN
                         actividad a ON a.id_actividad = dc.id_actividad
                             JOIN
                         materia m2 ON m2.id_materia = a.id_materia
                     WHERE
                             dc.id_alumno = idAlumno -- El ID del alumno
                       AND dc.id_bimestre = idBimestre -- id bimestre
                       AND m2.id_materia = m.id_materia
                       AND dc.id_actividad IS NOT NULL -- que no sea null, significa que es calificacion de actividad
                 ), 0) puntos_actividades,
        COALESCE((
                     SELECT
                         -- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
                         IF((SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL, puntosActitudinal, SUM(ca.puntos_sumados) - SUM(ca.puntos_restados))
                     FROM
                         control_actitudinal ca
                     WHERE
                             ca.id_alumno = idAlumno -- El ID del alumno
                       AND ca.id_bimestre = idBimestre -- id bimestre
                       AND ca.id_materia = m.id_materia
                 ), 0) puntos_conducta,
        COALESCE((
                     SELECT
                         SUM(dc.puntos_obtenidos)
                     FROM
                         detalle_calificacion dc
                             JOIN
                         evaluacion e  ON e.id_evaluacion  = dc.id_evaluacion
                             JOIN
                         materia m2 ON m2.id_materia = e.id_materia
                     WHERE
                             dc.id_alumno = idAlumno -- El ID del alumno
                       AND dc.id_bimestre = idBimestre -- id bimestre
                       AND m2.id_materia = m.id_materia
                       AND dc.id_evaluacion  IS NOT NULL -- que no sea null, significa que es evaluacion
                 ), 0) puntos_evaluaciones
    FROM
        materia m
    WHERE
            m.id_grado = (SELECT s.id_grado FROM alumno a JOIN seccion s on a.id_seccion = s.id_seccion WHERE a.id_alumno = idAlumno ) -- El ID del alumno
      AND m.estado = 1; -- que no este eliminado

END

-- changeset liquibase:jaasiel-13 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_notas_finales;

-- changeset liquibase:jaasiel-14 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE proc_reporte_notas_finales(
	idBimestre_1 BIGINT,
	idBimestre_2 BIGINT,
	idBimestre_3 BIGINT,
	idBimestre_4 BIGINT,
	idAlumno BIGINT
)
BEGIN


	DECLARE puntosActitudinal_bim_1 DOUBLE(16,2);
	DECLARE puntosActitudinal_bim_2 DOUBLE(16,2);
	DECLARE puntosActitudinal_bim_3 DOUBLE(16,2);
	DECLARE puntosActitudinal_bim_4 DOUBLE(16,2);

	-- consultar los puntos actitudinal configurados en el bimestre
	SELECT b.puntos_actitudinal INTO puntosActitudinal_bim_1 FROM bimestre b WHERE b.id_bimestre = idBimestre_1;
	SELECT b.puntos_actitudinal INTO puntosActitudinal_bim_2 FROM bimestre b WHERE b.id_bimestre = idBimestre_2;
	SELECT b.puntos_actitudinal INTO puntosActitudinal_bim_3 FROM bimestre b WHERE b.id_bimestre = idBimestre_3;
	SELECT b.puntos_actitudinal INTO puntosActitudinal_bim_4 FROM bimestre b WHERE b.id_bimestre = idBimestre_4;



	SELECT
		m.descripcion materia,

		-- PARA PRIMER BIMESTRE
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				actividad a ON a.id_actividad = dc.id_actividad
			JOIN
				materia m2 ON m2.id_materia = a.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_1 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_actividad IS NOT NULL -- que no sea null, significa que es calificacion de actividad
		), 0) +
		COALESCE((
			SELECT
				-- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
				IF((SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL, puntosActitudinal_bim_1, SUM(ca.puntos_sumados) - SUM(ca.puntos_restados))
			FROM
				control_actitudinal ca
			WHERE
				ca.id_alumno = idAlumno -- El ID del alumno
				AND ca.id_bimestre = idBimestre_1 -- id bimestre
				AND ca.id_materia = m.id_materia
		), 0) +
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				evaluacion e  ON e.id_evaluacion  = dc.id_evaluacion
			JOIN
				materia m2 ON m2.id_materia = e.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_1 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_evaluacion  IS NOT NULL -- que no sea null, significa que es evaluacion
		), 0) bimestre_1,

		-- PARA SEGUNDO BIMESTRE
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				actividad a ON a.id_actividad = dc.id_actividad
			JOIN
				materia m2 ON m2.id_materia = a.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_2 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_actividad IS NOT NULL -- que no sea null, significa que es calificacion de actividad
		), 0) +
		COALESCE((
			SELECT
				-- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
				IF((SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL, puntosActitudinal_bim_2, SUM(ca.puntos_sumados) - SUM(ca.puntos_restados))
			FROM
				control_actitudinal ca
			WHERE
				ca.id_alumno = idAlumno -- El ID del alumno
				AND ca.id_bimestre = idBimestre_2 -- id bimestre
				AND ca.id_materia = m.id_materia
		), 0) +
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				evaluacion e  ON e.id_evaluacion  = dc.id_evaluacion
			JOIN
				materia m2 ON m2.id_materia = e.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_2 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_evaluacion  IS NOT NULL -- que no sea null, significa que es evaluacion
		), 0) bimestre_2,

		-- PARA TERCER BIMESTRE
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				actividad a ON a.id_actividad = dc.id_actividad
			JOIN
				materia m2 ON m2.id_materia = a.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_3 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_actividad IS NOT NULL -- que no sea null, significa que es calificacion de actividad
		), 0) +
		COALESCE((
			SELECT
				-- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
				IF((SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL, puntosActitudinal_bim_3, SUM(ca.puntos_sumados) - SUM(ca.puntos_restados))
			FROM
				control_actitudinal ca
			WHERE
				ca.id_alumno = idAlumno -- El ID del alumno
				AND ca.id_bimestre = idBimestre_3 -- id bimestre
				AND ca.id_materia = m.id_materia
		), 0) +
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				evaluacion e  ON e.id_evaluacion  = dc.id_evaluacion
			JOIN
				materia m2 ON m2.id_materia = e.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_3 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_evaluacion  IS NOT NULL -- que no sea null, significa que es evaluacion
		), 0) bimestre_3,


		-- PARA CUARTO BIMESTRE
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				actividad a ON a.id_actividad = dc.id_actividad
			JOIN
				materia m2 ON m2.id_materia = a.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_4 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_actividad IS NOT NULL -- que no sea null, significa que es calificacion de actividad
		), 0) +
		COALESCE((
			SELECT
				-- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
				IF((SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL, puntosActitudinal_bim_4, SUM(ca.puntos_sumados) - SUM(ca.puntos_restados))
			FROM
				control_actitudinal ca
			WHERE
				ca.id_alumno = idAlumno -- El ID del alumno
				AND ca.id_bimestre = idBimestre_4 -- id bimestre
				AND ca.id_materia = m.id_materia
		), 0) +
		COALESCE((
			SELECT
				SUM(dc.puntos_obtenidos)
			FROM
				detalle_calificacion dc
			JOIN
				evaluacion e  ON e.id_evaluacion  = dc.id_evaluacion
			JOIN
				materia m2 ON m2.id_materia = e.id_materia
			WHERE
				dc.id_alumno = idAlumno -- El ID del alumno
				AND dc.id_bimestre = idBimestre_4 -- id bimestre
				AND m2.id_materia = m.id_materia
				AND dc.id_evaluacion  IS NOT NULL -- que no sea null, significa que es evaluacion
		), 0) bimestre_4
	FROM
		materia m
	WHERE
		m.id_grado = (SELECT s.id_grado FROM alumno a JOIN seccion s on a.id_seccion = s.id_seccion WHERE a.id_alumno = idAlumno ) -- El ID del alumno
		AND m.estado = 1; -- que no este eliminado


END


-- changeset liquibase:jaasiel-15 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_actitudinal_alumno;

-- changeset liquibase:jaasiel-16 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE proc_reporte_actitudinal_alumno(
    idBimestre BIGINT,
    idAlumno BIGINT
)
BEGIN

    SELECT
        ca.descripcion descripcion ,
        ca.fecha fecha,
        m.descripcion materia,
        ca.puntos_restados puntosRestados,
        ca.puntos_sumados puntosSumados,
        ca.puntos_actuales puntosActuales
    FROM
        control_actitudinal ca
            JOIN materia m ON
                m.id_materia = ca.id_materia
    WHERE
            ca.id_alumno = idAlumno -- ID alumno
      AND ca.id_bimestre = idBimestre -- ID bimestre
    ORDER BY ca.fecha DESC;

END
