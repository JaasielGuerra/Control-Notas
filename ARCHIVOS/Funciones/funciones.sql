 USE `db_control_notas`;
DROP function IF EXISTS `porcentaje_asistencia`;

DELIMITER $$
USE `db_control_notas`$$
CREATE FUNCTION `porcentaje_asistencia` (
	id_alumno bigint
)
RETURNS DOUBLE(16,2)
BEGIN

RETURN (
	SELECT 
		((SELECT 
				COUNT(d.id_detalle_listado)
			FROM
				detalle_listado d
			WHERE
				id_alumno = id_alumno) / c.dias_base_asistencia) * 100 AS porcentaje_asistencia
	FROM
		ciclo_escolar c
	WHERE
		c.estado = 1
);
END$$

DELIMITER ;
------------------------------------------------------------

USE `db_control_notas`;
DROP function IF EXISTS `libro_disponible`;

DELIMITER $$
USE `db_control_notas`$$
CREATE FUNCTION `libro_disponible` (
	id_libro bigint
)
RETURNS INTEGER
BEGIN

RETURN (
	SELECT 
		COUNT(l.id_libro)
	FROM
		libro l
	WHERE
		l.id_libro = id_libro AND l.disponibilidad = 1
);
END$$

DELIMITER ;
-------------------------------------------

USE `db_control_notas`;
DROP function IF EXISTS `bimestre_aperturado`;

DELIMITER $$
USE `db_control_notas`$$
CREATE FUNCTION `bimestre_aperturado` (
	id_bimestre bigint
)
RETURNS INTEGER
BEGIN

RETURN (
	SELECT 
		COUNT(b.id_bimestre)
	FROM
		bimestre b
	WHERE
		b.id_bimestre = id_bimestre AND b.estado = 1
);
END$$

DELIMITER ;