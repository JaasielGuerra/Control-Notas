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