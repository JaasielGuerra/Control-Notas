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

-- changeset liquibase:jaasiel-31
ALTER TABLE `db_control_notas`.`detalle_lectura`
    DROP FOREIGN KEY `fk_detalle_lectura_control_lectura1`;
ALTER TABLE `db_control_notas`.`detalle_lectura`
    DROP COLUMN `id_control_lectura`,
    ADD COLUMN `id_bimestre` BIGINT NOT NULL AFTER `id_alumno`,
    CHANGE COLUMN `fecha` `fecha` DATE NOT NULL ,
    CHANGE COLUMN `tipo_operacion` `tipo_operacion` INT(11) NOT NULL COMMENT '1 = entrega de libro\\n2 = paginas leidas\\n3 = libro terminado de leer' ,
    CHANGE COLUMN `paginas_leidas` `paginas_leidas` INT(11) NOT NULL ,
    ADD INDEX `fk_detalle_lectura_bimestre1_idx` (`id_bimestre` ASC) ,
    DROP INDEX `fk_detalle_lectura_control_lectura1_idx` ;
;
ALTER TABLE `db_control_notas`.`detalle_lectura`
    ADD CONSTRAINT `fk_detalle_lectura_bimestre1`
        FOREIGN KEY (`id_bimestre`)
            REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
drop table control_lectura;

-- changeset liquibase:jaasiel-32 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS proc_consulta_grados_lectura;

-- changeset liquibase:jaasiel-33 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE proc_consulta_grados_lectura()
BEGIN

    select
        g.id_grado as idGrado,
        g.descripcion as gradoDescripcion,
        (
            select
                count(a.id_alumno)
            from
                alumno a
                    inner join seccion s2 on
                        s2.id_seccion = a.id_seccion
            where
                    s2.id_grado = g.id_grado
              and a.estado = 1) as countAlumnos
    from
        grado g
    where
            g.estado = 1;

END

-- changeset liquibase:jaasiel-34 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_listar_alumnos_lectura;

-- changeset liquibase:jaasiel-35 endDelimiter:$$\nDELIMITER ;
CREATE  PROCEDURE `proc_listar_alumnos_lectura`(
    estado_alumno BIGINT,
    id_grado BIGINT

) BEGIN
    SELECT
        a.id_alumno idAlumno,
        a.nombre nombre,
        a.apellido apellido,
        a.codigo_alumno codigoAlumno,
        (
            SELECT
                COUNT(DISTINCT dl.id_libro)
            FROM
                detalle_lectura dl
            WHERE
                    dl.id_alumno = a.id_alumno
              AND dl.tipo_operacion = 3) librosLeidos,
        COALESCE (func_consultar_libro_actual_alumno(a.id_alumno),
                  'NINGÚN LIBRO') libroActual,
        COALESCE ((
                      SELECT
                          IF(dl2.tipo_operacion IN(1, 2), dl2.paginas_leidas , 0)
                      FROM
                          detalle_lectura dl2
                      WHERE
                              dl2.id_alumno = a.id_alumno
                      ORDER BY
                          dl2.id_detalle_lectura DESC
                      LIMIT 1 ),
                  0) paginasLeidas
    FROM
        alumno a
            JOIN seccion s on
                s.id_seccion = a.id_seccion
    WHERE
            a.estado = estado_alumno
      AND s.id_grado = id_grado;
END

-- changeset liquibase:jaasiel-36 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_consultar_libro_actual_alumno;

-- changeset liquibase:jaasiel-37 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION func_consultar_libro_actual_alumno( id_alumno BIGINT ) RETURNS VARCHAR(200) BEGIN RETURN (
    SELECT
        IF(dl2.tipo_operacion IN(1, 2), l2.nombre , 'NINGÚN LIBRO') libroActual
    FROM
        detalle_lectura dl2
            JOIN libro l2 ON
                l2.id_libro = dl2.id_libro
    WHERE
            dl2.id_alumno = id_alumno
    ORDER BY
        dl2.id_detalle_lectura DESC
    LIMIT 1 );
END

-- changeset liquibase:jaasiel-38 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_alumno_tiene_libro_asignado;

-- changeset liquibase:jaasiel-39 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION func_alumno_tiene_libro_asignado( id_alumno BIGINT ) RETURNS INT BEGIN RETURN(
    SELECT
        IF(dl2.tipo_operacion IN(1, 2), 1 , 0) tieneLibro
    FROM
        detalle_lectura dl2
    WHERE
            dl2.id_alumno = id_alumno
    ORDER BY
        dl2.id_detalle_lectura DESC
    LIMIT 1 );

END

-- changeset liquibase:jaasiel-40 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_id_ultimo_libro_alumno;

-- changeset liquibase:jaasiel-41 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION db_control_notas.func_obtener_id_ultimo_libro_alumno(
    id_alumno BIGINT
)
    RETURNS BIGINT
BEGIN
    RETURN(

        SELECT
            l2.id_libro idLibro
        FROM
            detalle_lectura dl2
                JOIN libro l2 ON
                    l2.id_libro = dl2.id_libro
        WHERE
                dl2.id_alumno = id_alumno
        ORDER BY
            dl2.id_detalle_lectura DESC
        LIMIT 1

    );
END

-- changeset liquibase:jaasiel-42 endDelimiter:\nDELIMITER $$
DROP TRIGGER IF EXISTS db_control_notas.trig_cambiar_disponibilidad_libro;

-- changeset liquibase:jaasiel-43 endDelimiter:$$\nDELIMITER ;
CREATE TRIGGER trig_cambiar_disponibilidad_libro
AFTER INSERT
ON detalle_lectura FOR EACH ROW

BEGIN

IF NEW.tipo_operacion = 1 THEN -- asignacion libro

	UPDATE
		libro l
	SET l.disponibilidad = 2 -- NO disponible
	WHERE
		l.id_libro = NEW.id_libro;

END IF;

IF NEW.tipo_operacion = 3 THEN -- libro terminado de leer

	UPDATE
		libro l
	SET l.disponibilidad = 1 -- SI disponible
	WHERE
		l.id_libro = NEW.id_libro;

END IF;

END

-- changeset liquibase:jaasiel-44 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_consulta_calificar_evaluaciones;

-- changeset liquibase:jaasiel-45 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE db_control_notas.proc_consulta_calificar_evaluaciones(
    id_bimestre LONG,
    estado_evaluacion INT,
    ids_materia VARCHAR(255)
)
BEGIN

    SET @query_ = CONCAT('
		SELECT
			e.id_evaluacion idEvaluacion,
			m.descripcion materia,
			g.descripcion gradoMateria,
			e.descripcion descripcionEvaluacion,
			e.ponderacion ponderacionEvaluacion,
			e.estado estadoEvaluacion,
			e.id_materia idMateria
		FROM
			evaluacion e
		JOIN materia m ON
			m.id_materia = e.id_materia
		JOIN grado g ON
			g.id_grado = m.id_grado
		WHERE
			e.id_bimestre = ', id_bimestre,
                         ' AND e.estado = ', estado_evaluacion,
                         ' AND e.id_materia IN(
                 ', ids_materia, ')');

    PREPARE stmt FROM @query_;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END

-- changeset liquibase:jaasiel-46 endDelimiter:\nDELIMITER $$
DROP TRIGGER IF EXISTS db_control_notas.trig_cambiar_estado_actividaes;

-- changeset liquibase:jaasiel-47 endDelimiter:$$\nDELIMITER ;
CREATE TRIGGER trig_cambiar_estado_actividaes
AFTER UPDATE
ON plan_trabajo FOR EACH ROW
BEGIN

	UPDATE
		actividad a
	SET
		a.estado = NEW.estado
	WHERE
		a.id_plan_trabajo = NEW.id_plan_trabajo;

END

-- changeset liquibase:jaasiel-48 endDelimiter:\nDELIMITER $$
DROP TRIGGER IF EXISTS db_control_notas.trig_cerrar_bimestres;

-- changeset liquibase:jaasiel-49 endDelimiter:$$\nDELIMITER ;
CREATE  TRIGGER trig_cerrar_bimestres
AFTER UPDATE
ON ciclo_escolar FOR EACH ROW
BEGIN

	IF NEW.estado = 2 THEN -- ciclo cerrado
		UPDATE
			bimestre b
		SET
			b.estado = NEW.estado
		WHERE
			b.id_ciclo_escolar = NEW.id_ciclo_escolar;
	END IF;

END

-- changeset liquibase:jaasiel-50
ALTER TABLE `db_control_notas`.`listado_asistencia`
ADD COLUMN `id_bimestre` BIGINT NOT NULL AFTER `id_usuario`,
ADD COLUMN `tipo` INT NOT NULL COMMENT '1 = ASISTENCIA\n2 = OTRO' AFTER `id_bimestre`,
ADD INDEX `fk_listado_asistencia_bimestre1_idx` (`id_bimestre` ASC) ;
;
ALTER TABLE `db_control_notas`.`listado_asistencia`
ADD CONSTRAINT `fk_listado_asistencia_bimestre1`
  FOREIGN KEY (`id_bimestre`)
  REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `db_control_notas`.`detalle_listado`
    ADD COLUMN `id_bimestre` BIGINT NOT NULL AFTER `id_listado_asistencia`,
    ADD COLUMN `motivo` VARCHAR(45) NULL AFTER `id_bimestre`,
    ADD INDEX `fk_detalle_listado_bimestre1_idx` (`id_bimestre` ASC) ;
;
ALTER TABLE `db_control_notas`.`detalle_listado`
    ADD CONSTRAINT `fk_detalle_listado_bimestre1`
        FOREIGN KEY (`id_bimestre`)
            REFERENCES `db_control_notas`.`bimestre` (`id_bimestre`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;


-- changeset liquibase:jaasiel-51 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_porcentaje_asistencia_alumno;

-- changeset liquibase:jaasiel-52 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-53 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_porcentaje_asistencia_alumno;

-- changeset liquibase:jaasiel-54 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-55
ALTER TABLE db_control_notas.detalle_listado MODIFY COLUMN id_detalle_listado BIGINT auto_increment NOT NULL;


-- changeset liquibase:jaasiel-56
DROP TABLE `db_control_notas`.`historial_actitudinal`;

-- changeset liquibase:jaasiel-57
DROP TABLE `db_control_notas`.`control_actitudinal`;

-- changeset liquibase:jaasiel-58
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


-- changeset liquibase:jaasiel-59 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_alumnos;

-- changeset liquibase:jaasiel-60 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-61 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_datos_alumno;

-- changeset liquibase:jaasiel-62 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-63 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_notas_por_bimestre;

-- changeset liquibase:jaasiel-64 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-65 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_notas_finales;

-- changeset liquibase:jaasiel-66 endDelimiter:$$\nDELIMITER ;
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

-- changeset liquibase:jaasiel-67 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_actitudinal_alumno;

-- changeset liquibase:jaasiel-68 endDelimiter:$$\nDELIMITER ;
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


-- changeset liquibase:jaasiel-69
set foreign_key_checks=0;
ALTER TABLE `db_control_notas`.`asignacion_materia`
ADD COLUMN `id_seccion` BIGINT NOT NULL AFTER `estado`,
ADD INDEX `fk_asignacion_materia_seccion1_idx` (`id_seccion` ASC) ;
ALTER TABLE `db_control_notas`.`asignacion_materia`
    ADD CONSTRAINT `fk_asignacion_materia_seccion1`
        FOREIGN KEY (`id_seccion`)
            REFERENCES `db_control_notas`.`seccion` (`id_seccion`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
set foreign_key_checks=1;

-- changeset liquibase:jaasiel-70 endDelimiter:\nDELIMITER $$
DROP TRIGGER IF EXISTS db_control_notas.trig_cerrar_bimestres;

-- changeset liquibase:jaasiel-71 endDelimiter:$$\nDELIMITER ;
CREATE TRIGGER trig_cerrar_bimestres
    AFTER UPDATE
    ON ciclo_escolar FOR EACH ROW
BEGIN

    IF NEW.estado = 2 THEN
        UPDATE
            bimestre b
        SET
            b.estado = NEW.estado
        WHERE
            b.id_ciclo_escolar = NEW.id_ciclo_escolar
            AND b.estado = 1; -- que esta abierto
    END IF;

END

-- changeset liquibase:jaasiel-72 endDelimiter:\nDELIMITER $$
DROP FUNCTION IF EXISTS db_control_notas.func_obtener_puntos_conducta_alumno;

-- changeset liquibase:jaasiel-73 endDelimiter:$$\nDELIMITER ;
CREATE FUNCTION func_obtener_puntos_conducta_alumno(
	idBimestre BIGINT,
	idAlumno BIGINT,
	idMateria BIGINT
)
RETURNS DOUBLE(16,2)
BEGIN

RETURN(
SELECT
	-- si no hay registros, devolver el 100% de actitudinal configurado en el bimestre
			IF(
				(SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)) IS NULL,
				(  -- consultar los puntos actitudinal configurados en el bimestre
					SELECT
						b.puntos_actitudinal
					FROM
						bimestre b
					WHERE
						b.id_bimestre = idBimestre),

	SUM(ca.puntos_sumados) - SUM(ca.puntos_restados)
			)
FROM
			control_actitudinal ca
WHERE
			ca.id_alumno = idAlumno
	-- El ID del alumno
	AND ca.id_bimestre = idBimestre
	-- id bimestre
	AND ca.id_materia = idMateria

);

END

-- changeset liquibase:jaasiel-74 endDelimiter:\nDELIMITER $$
DROP PROCEDURE IF EXISTS db_control_notas.proc_reporte_actitudinal_alumno;

-- changeset liquibase:jaasiel-75 endDelimiter:$$\nDELIMITER ;
CREATE PROCEDURE `proc_reporte_actitudinal_alumno`(
    idBimestre BIGINT,
    idAlumno BIGINT,
    idMateria BIGINT
)
BEGIN

    -- por materia
    IF idMateria IS NOT NULL THEN

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
                ca.id_alumno = idAlumno
          AND ca.id_bimestre = idBimestre
          AND m.id_materia = idMateria
        ORDER BY ca.fecha DESC;

        -- todos los registros
    ELSE

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
                ca.id_alumno = idAlumno
          AND ca.id_bimestre = idBimestre
        ORDER BY ca.fecha DESC;

    END IF;
END

-- changeset liquibase:jaasiel-76
ALTER TABLE `db_control_notas`.`alumno`
ADD COLUMN `encargado` INT NOT NULL COMMENT 'VALORES:\n\n1 = PADRE\n2 = MADRE' AFTER `estado_expediente`;
