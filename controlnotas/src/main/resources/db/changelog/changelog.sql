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


