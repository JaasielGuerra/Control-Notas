USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_grado`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_grado` (
descripcion varchar(150),
id_usuario bigint

)
BEGIN


INSERT INTO `db_control_notas`.`grado`
(`id_grado`,
`descripcion`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_usuario`)
VALUES
(null,
descripcion,
1,
current_date(),
current_time(),
id_usuario);

END$$

DELIMITER ;

----------------------------------------------------------------------------------------------------

USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_alumno`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_alumno` (
nombre varchar(150),
apellido varchar(150),
direccion varchar(150),
fecha_nacimiento date,
codigo_alumno varchar(45),
id_seccion bigint,
observacion_expediente varchar(150),
id_usuario bigint
)
BEGIN

INSERT INTO `db_control_notas`.`alumno`
(`id_alumno`,
`nombre`,
`apellido`,
`direccion`,
`fecha_nacimiento`,
`codigo_alumno`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_seccion`,
`observacion_expediente`,
`id_usuario`)
VALUES
(null,
nombre,
apellido,
direccion,
fecha_nacimiento,
codigo_alumno,
1,
current_date(),
current_time(),
id_seccion,
observacion_expediente,
id_usuario);


END$$

DELIMITER ;
------------------------------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_seccion`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_seccion` (
descripcion varchar(150),
id_grado bigint,
id_usuario bigint
)
BEGIN
INSERT INTO `db_control_notas`.`seccion`
(`id_seccion`,
`descripcion`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_grado`,
`id_usuario`)
VALUES
(null,
descripcion,
1,
current_date(),
current_time(),
id_grado,
id_usuario);

END$$

DELIMITER ;
--------------------------------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_rol`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_rol` (
descripcion varchar(100)
)
BEGIN
INSERT INTO `db_control_notas`.`rol`
(`id_rol`,
`descripcion`,
`estado`,
`fecha_commit`,
`hora_commit`)
VALUES
(null,
descripcion,
1,
current_date(),
current_time());

END$$

DELIMITER ;


--------------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_materia`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_nateria` (
descripcion varchar(150),
id_grado BIGINT,
id_usuario bigint
)
BEGIN
INSERT INTO `db_control_notas`.`materia`
(`id_materia`,
`descripcion`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_grado`,
`id_usuario`)
VALUES
(null,
descripcion,
1,
current_date(),
current_time(),
id_grado,
id_usuario);

END$$

DELIMITER ;

------------------------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_usuario`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_usuario` (
nombre_completo varchar(150),
user varchar(45),
password varchar(45),
id_rol bigint


)
BEGIN
INSERT INTO `db_control_notas`.`usuario`
(`id_usuario`,
`nombre_completo`,
`user`,
`password`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_rol`)
VALUES
(null,
nombre_completo,
user,
password,
1,
current_date,
current_time,
id_rol);

END$$

DELIMITER ;

-----------------------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_libro`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_libro` (
nombre varchar(150),
descripcion varchar(150),
disponibilidad int,
id_usuario bigint
)
BEGIN
INSERT INTO `db_control_notas`.`libro`
(`id_libro`,
`nombre`,
`descripcion`,
`estado`,
`fecha_commit`,
`hora_commit`,
`disponibilidad`,
`id_usuario`)
VALUES
(null,
nombre,
descripcion,
1,
current_date(),
current_time(),
disponibilidad,
id_usuario);

END$$

DELIMITER ;

------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_ciclo_escolar`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_ciclo_escolar` (
anio int,
fecha_apertura date,
fecha_cierre date,
dias_base_asistencia int,
id_usuario bigint
)
BEGIN
INSERT INTO `db_control_notas`.`ciclo_escolar`
(`id_ciclo_escolar`,
`anio`,
`estado`,
`fecha_apertura`,
`fecha_cierre`,
`dias_base_asistencia`,
`id_usuario`)
VALUES
(null,
anio,
1,
fecha_apertura,
fecha_cierre,
dias_base_asistencia,
id_usuario);

END$$

DELIMITER ;

------------------------------------------------------------------------
USE `db_control_notas`;
DROP procedure IF EXISTS `insertar_bimestre`;

DELIMITER $$
USE `db_control_notas`$$
CREATE PROCEDURE `insertar_bimestre` (
descripcion varchar(150),
puntos_base double(16,2),
puntos_actividades double(16,2),
puntos_actitudinal double(16,2),
puntos_evaluaciones double(16,2),
fecha_apertura date,
fecha_cierre date,
id_ciclo_escolar bigint,
id_usuario bigint
)
BEGIN
INSERT INTO `db_control_notas`.`bimestre`
(`id_bimestre`,
`descripcion`,
`puntos_base`,
`puntos_actividades`,
`puntos_actitudinal`,
`puntos_evaluaciones`,
`fecha_apertura`,
`fecha_cierre`,
`estado`,
`fecha_commit`,
`hora_commit`,
`id_ciclo_escolar`,
`id_usuario`)
VALUES
(null,
descripcion,
puntos_base,
puntos_actividades,
puntos_actitudinal,
puntos_evaluaciones,
fecha_apertura,
fecha_cierre,
1,
current_date(),
current_time(),
id_ciclo_escolar,
id_usuario);

END$$

DELIMITER ;

