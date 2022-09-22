-- liquibase formatted sql

-- changeset liquibase:jaasiel-1
INSERT INTO `db_control_notas`.`rol` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`) VALUES ('ADMIN', '1', '2022-08-04', '09:23');
INSERT INTO `db_control_notas`.`usuario` (`nombre_completo`, `user`, `password`, `estado`, `fecha_commit`, `hora_commit`, `id_rol`) VALUES ('ADMINISTRAR SISTEMA', 'admin', 'admin', '1', '2022-08-04', '10:20', '1');

-- changeset liquibase:jaasiel-2
INSERT INTO `db_control_notas`.`grado` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`, `id_grado_siguiente`) VALUES ('PRIMERO BÁSICO', '1', '2022-08-04', '10:30', '1', NULL);
INSERT INTO `db_control_notas`.`grado` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`, `id_grado_anterior`, `id_grado_siguiente`) VALUES ('SEGUNDO BÁSICO', '1', '2022-08-04', '10:30', '1', NULL, NULL);
INSERT INTO `db_control_notas`.`grado` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`, `id_grado_anterior`) VALUES ('TERCERO BÁSICO', '1', '2022-08-04', '10:30', '1', NULL);

-- changeset liquibase:jaasiel-3
UPDATE `db_control_notas`.`grado` SET `id_grado_anterior` = NULL, `id_grado_siguiente` = '2' WHERE (`id_grado` = '1');
UPDATE `db_control_notas`.`grado` SET `id_grado_anterior` = '1', `id_grado_siguiente` = '3' WHERE (`id_grado` = '2');
UPDATE `db_control_notas`.`grado` SET `id_grado_anterior` = '2', `id_grado_siguiente` = NULL WHERE (`id_grado` = '3');

-- changeset liquibase:jaasiel-4
INSERT INTO `db_control_notas`.`seccion` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('A', '1', '2022-08-04', '11:00', '1', '1');
INSERT INTO `db_control_notas`.`seccion` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('A', '1', '2022-08-04', '11:00', '2', '1');
INSERT INTO `db_control_notas`.`seccion` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('A', '1', '2022-08-04', '11:00', '3', '1');

-- changeset liquibase:jaasiel-5
UPDATE `db_control_notas`.`rol` SET `descripcion` = 'DIRECTOR' WHERE (`id_rol` = '1');
INSERT INTO `db_control_notas`.`rol` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`) VALUES ('DOCENTE', '1', '2022-08-04', '09:23:00');
ALTER TABLE `db_control_notas`.`usuario`
    CHANGE COLUMN `password` `password` VARCHAR(150) NOT NULL ;
INSERT INTO `db_control_notas`.`usuario` (`nombre_completo`, `user`, `password`, `estado`, `fecha_commit`, `hora_commit`, `id_rol`) VALUES ('DOCENTE INSTITUTO', 'docente', '$2a$10$zo7jMwlS8nTdaO3MeCvK6.vNFtWfRTN2sA4lal435ziqtSWWDJzcK', '1', '2022-08-04', '10:20:00', '2');
UPDATE `db_control_notas`.`usuario` SET `nombre_completo` = 'DIRECTOR INSTITUTO', `user` = 'director', `password` = '$2a$10$4qoZhgNKSjjqLgpS4zUosOTrMKS.48or1CaaFdR80sCkle3U4tr6m' WHERE (`id_usuario` = '1');

-- changeset liquibase:jaasiel-6
UPDATE `db_control_notas`.`rol` SET `descripcion` = 'ROLE_DIRECTOR' WHERE (`id_rol` = '1');
UPDATE `db_control_notas`.`rol` SET `descripcion` = 'ROLE_DOCENTE' WHERE (`id_rol` = '2');

-- changeset liquibase:jaasiel-7
ALTER TABLE `db_control_notas`.`alumno`
    ADD COLUMN `estado_expediente` INT NULL COMMENT '1 = completo\n0 = incompleto' AFTER `id_usuario`;

-- changeset liquibase:jaasiel-8 endDelimiter:;\nDELIMITER $$
DROP function IF EXISTS `db_control_notas`.`func_contar_alumnos_expediente_incompleto`

-- changeset liquibase:jaasiel-9 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION `func_contar_alumnos_expediente_incompleto`() RETURNS int(11)
BEGIN
    RETURN (

        SELECT
            COUNT(a.id_alumno)
        FROM
            alumno a
        WHERE
                a.estado = 1
          AND
            (
                        a.estado_expediente = 0
                    OR
                        a.estado_expediente IS NULL
                )
    );
END

-- changeset liquibase:jaasiel-10
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('CÓDIGO ESTUDIANTIL', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('CERTIFICADO DE GRADO', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('DIPLOMA DE SEXTO', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('6 CERTIFICADOS DE PRIMARIA', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('COPIA DPI ENCARGADO', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('FE DE EDAD DEL ALUMNO', '1', '2022-08-23', '09:46', '1');
INSERT INTO `db_control_notas`.`documento_expediente` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_usuario`) VALUES ('3 ETAPAS DE PRE PRIMARIA (ANEXO)', '1', '2022-08-23', '09:46', '1');

-- changeset liquibase:jaasiel-11
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('MATEMÁTICAS', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('ESPAÑOL', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CULTURA Y LENGUAS MAYA GARÍFUNA Y XINCA', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS NATURALES', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS SOCIALES, CIVISMO Y CULTURA CIUDADANA', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('LENGUA EXTRANJERA (INGLÉS)', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EXPRESIÓN Y APRECIACIÓN ARTÍSTICA', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EMPRENDIMIENTO PARA LA PRODUCTIVIDAD', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('TECNOLOGÍA EDUCATIVA', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EDUCACIÓN FÍSICA', '1', '2022-08-28', '20:07', '1', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('MATEMÁTICAS', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('ESPAÑOL', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CULTURA Y LENGUAS MAYA GARÍFUNA Y XINCA', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS NATURALES', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS SOCIALES, CIVISMO Y CULTURA CIUDADANA', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('LENGUA EXTRANJERA (INGLÉS)', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EXPRESIÓN Y APRECIACIÓN ARTÍSTICA', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EMPRENDIMIENTO PARA LA PRODUCTIVIDAD', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('TECNOLOGÍA EDUCATIVA', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EDUCACIÓN FÍSICA', '1', '2022-08-28', '20:07', '2', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('MATEMÁTICAS', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('ESPAÑOL', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CULTURA Y LENGUAS MAYA GARÍFUNA Y XINCA', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS NATURALES', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('CIENCIAS SOCIALES, CIVISMO Y CULTURA CIUDADANA', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('LENGUA EXTRANJERA (INGLÉS)', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EXPRESIÓN Y APRECIACIÓN ARTÍSTICA', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EMPRENDIMIENTO PARA LA PRODUCTIVIDAD', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('TECNOLOGÍA EDUCATIVA', '1', '2022-08-28', '20:07', '3', '1');
INSERT INTO `db_control_notas`.`materia` (`descripcion`, `estado`, `fecha_commit`, `hora_commit`, `id_grado`, `id_usuario`) VALUES ('EDUCACIÓN FÍSICA', '1', '2022-08-28', '20:07', '3', '1');


-- changeset liquibase:jaasiel-12
INSERT INTO `db_control_notas`.`ciclo_escolar` (`anio`, `estado`, `fecha_apertura`, `fecha_cierre`, `dias_base_asistencia`, `id_usuario`) VALUES ('2022', '1', '2022-01-15', NULL, '200', '1');
INSERT INTO `db_control_notas`.`bimestre` (`descripcion`, `puntos_base`, `puntos_actividades`, `puntos_actitudinal`, `puntos_evaluaciones`, `fecha_apertura`, `estado`, `fecha_commit`, `hora_commit`, `id_ciclo_escolar`, `id_usuario`) VALUES ('PRIMER BIMESTRE', '100', '60', '10', '30', '2022-01-15', '1', '2022-08-28', '21:05', '1', '1');

-- changeset liquibase:jaasiel-13 endDelimiter:;\nDELIMITER $$
DROP procedure IF EXISTS `proc_obtener_grados_sin_plan_trabajo`

-- changeset liquibase:jaasiel-14 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE `proc_obtener_grados_sin_plan_trabajo` (
    id_bimestre bigint
)
BEGIN

    SELECT
        g.id_grado AS id, g.descripcion AS descripcion
    FROM
        grado g
    WHERE
            (SELECT
                 COUNT(p.id_plan_trabajo)
             FROM
                 plan_trabajo p
             WHERE
                     p.id_bimestre = id_bimestre
               AND p.id_grado = g.id_grado) = 0;

END


-- changeset liquibase:jaasiel-15 endDelimiter:;\nDELIMITER $$
DROP procedure IF EXISTS `proc_obtener_grados_sin_plan_trabajo`

-- changeset liquibase:jaasiel-16 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE `proc_obtener_grados_sin_plan_trabajo` (
    id_bimestre bigint
)
BEGIN

    SELECT
        g.id_grado AS id, g.descripcion AS descripcion
    FROM
        grado g
    WHERE
            (SELECT
                 COUNT(p.id_plan_trabajo)
             FROM
                 plan_trabajo p
             WHERE
                     p.id_bimestre = id_bimestre
               AND p.id_grado = g.id_grado
               AND p.estado = 1) = 0;
END

-- changeset liquibase:jaasiel-17
ALTER TABLE `db_control_notas`.`actividad`
ADD COLUMN `estado` INT NOT NULL AFTER `id_usuario`;

-- changeset liquibase:jaasiel-18 endDelimiter:\nDELIMITER $$
DROP function IF EXISTS `func_existe_plan_trabajo`;

-- changeset liquibase:jaasiel-19 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION `func_existe_plan_trabajo` (
    id_bimestre BIGINT,
    id_grado BIGINT
)
    RETURNS INTEGER
BEGIN

    RETURN (

        SELECT
            COUNT(p.id_plan_trabajo)
        FROM
            plan_trabajo p
        WHERE
                p.id_bimestre = id_bimestre AND p.id_grado = id_grado
          AND p.estado = 1 -- plan activo

    );
END

-- changeset liquibase:jaasiel-20
INSERT INTO `institucion` VALUES (1,'INEB DE TELESECUNDARIA \"MANUEL JOSE ARCE\"','ALDEA LAS VIÑAS, LOS AMATES, IZABAL','18-05-2419-40',1,'2022-09-11','23:44:31','José Alberto Acevedo Arroyo','','','BÁSICO','OFICIAL','VESPERTINA');

-- changeset liquibase:jaasiel-21
ALTER TABLE `db_control_notas`.`evaluacion`
DROP FOREIGN KEY `fk_evaluacion_seccion1`;
ALTER TABLE `db_control_notas`.`evaluacion`
DROP COLUMN `id_seccion`,
ADD COLUMN `id_bimestre` BIGINT NOT NULL AFTER `id_usuario`,
ADD INDEX `fk_evaluacion_bimestre1_idx` (`id_bimestre` ASC),
DROP INDEX `fk_evaluacion_seccion1_idx` ;
;
ALTER TABLE `db_control_notas`.`evaluacion`
    ADD CONSTRAINT `fk_evaluacion_bimestre1`
        FOREIGN KEY (`id_bimestre`)
            REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

-- changeset liquibase:jaasiel-22
INSERT INTO `db_control_notas`.`tipo_evaluacion` (`id_tipo_evaluacion`, `descripcion`) VALUES ('1', 'EXÁMEN');
INSERT INTO `db_control_notas`.`tipo_evaluacion` (`id_tipo_evaluacion`, `descripcion`) VALUES ('2', 'LABORATORIO');

-- changeset liquibase:jaasiel-23
INSERT INTO `db_control_notas`.`asignacion_materia` (`id_asignacion_materia`, `id_usuario`, `id_materia`, `estado`) VALUES ('1', '1', '2', '1');
INSERT INTO `db_control_notas`.`asignacion_materia` (`id_asignacion_materia`, `id_usuario`, `id_materia`, `estado`) VALUES ('2', '1', '14', '1');

-- changeset liquibase:jaasiel-24 endDelimiter:\nDELIMITER $$
DROP function IF EXISTS `func_puntos_disponibles_evaluaciones`;

-- changeset liquibase:jaasiel-25 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION `func_puntos_disponibles_evaluaciones` (
    id_materia long,
    id_bimestre long,
    id_evaluacion_excluir long
)
    RETURNS DOUBLE(16,2)
BEGIN

    DECLARE puntos_evaluaciones DOUBLE;


    SELECT
        b.puntos_evaluaciones INTO puntos_evaluaciones
    FROM
        bimestre b
    WHERE
            b.id_bimestre = id_bimestre;


    IF  id_evaluacion_excluir IS NOT NULL THEN
        RETURN (

            -- Calcular puntos disponibles excluyendo una evaluacion por su ID
            SELECT
                (puntos_evaluaciones - COALESCE(SUM(e.ponderacion), 0.0)) AS total_evaluaciones
            FROM
                evaluacion e
            WHERE
                    e.id_materia = id_materia
              AND e.id_bimestre = id_bimestre
              AND e.estado = 1
              AND NOT e.id_evaluacion = id_evaluacion_excluir
        );
    ELSE

        RETURN (

            -- Calcular puntos disponibles tomando en cuenta todas las evaluaciones segun materia
            SELECT
                (puntos_evaluaciones - COALESCE(SUM(e.ponderacion), 0.0)) AS total_evaluaciones
            FROM
                evaluacion e
            WHERE
                    e.id_materia = id_materia
              AND e.id_bimestre = id_bimestre
              AND e.estado = 1
        );

    END IF;
END

-- changeset liquibase:jaasiel-26
ALTER TABLE `db_control_notas`.`detalle_calificacion`
DROP FOREIGN KEY `fk_detalle_calificacion_cuado_calificacion1`;
ALTER TABLE `db_control_notas`.`detalle_calificacion`
    DROP COLUMN `id_cuadro_calificacion`,
    ADD COLUMN `id_bimestre` BIGINT NOT NULL AFTER `id_usuario`,
    ADD INDEX `fk_detalle_calificacion_bimestre1_idx` (`id_bimestre` ASC) ,
    DROP INDEX `fk_detalle_calificacion_cuado_calificacion1_idx` ;
;
ALTER TABLE `db_control_notas`.`detalle_calificacion`
    ADD CONSTRAINT `fk_detalle_calificacion_bimestre1`
        FOREIGN KEY (`id_bimestre`)
            REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
DROP TABLE `db_control_notas`.`cuadro_calificacion`;

-- changeset liquibase:jaasiel-27 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_consulta_calificar_actividades;

-- changeset liquibase:jaasiel-28 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE proc_consulta_calificar_actividades(
    id_bimestre LONG,
    estado_actividad INT,
    ids_materia VARCHAR(255)
)
BEGIN

    SET @query_ = CONCAT('
		SELECT
			a.id_actividad,
			m.descripcion materia,
			g.descripcion grado_materia,
			a.descripcion_actividad,
			a.valor_actividad,
			a.estado estado_actividad,
			a.id_materia
		FROM
			actividad a
		JOIN plan_trabajo pt ON
			pt.id_plan_trabajo = a.id_plan_trabajo
		JOIN materia m ON
			m.id_materia = a.id_materia
		JOIN grado g ON
			g.id_grado = m.id_grado
		WHERE
			pt.id_bimestre = ', id_bimestre,
                         ' AND a.estado = ', estado_actividad,
                         ' AND a.id_materia IN(
                 ', ids_materia, ')');

    PREPARE stmt FROM @query_;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END

-- changeset liquibase:jaasiel-29
INSERT INTO `db_control_notas`.`asignacion_materia` (`id_asignacion_materia`, `id_usuario`, `id_materia`, `estado`) VALUES ('3', '1', '24', '1');

-- changeset liquibase:jaasiel-30
INSERT INTO `db_control_notas`.`asignacion_materia` (`id_asignacion_materia`, `id_usuario`, `id_materia`, `estado`) VALUES ('4', '1', '21', '1');
