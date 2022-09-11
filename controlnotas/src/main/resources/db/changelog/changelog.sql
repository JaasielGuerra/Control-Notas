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

