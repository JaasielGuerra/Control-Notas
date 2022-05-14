USE `db_control_notas`;
CREATE  OR REPLACE VIEW `alumnos_grado` AS
SELECT 
a.id_alumno as ID, a.nombre as Nombre,a.apellido as Apellido, a.codigo_alumno as Codigo, a.estado as Estado, g.descripcion as Grado, s.descripcion as Seccion

    
FROM
    alumno a
INNER JOIN 
	seccion s 
    on a.id_seccion = s.id_seccion
INNER JOIN 
	grado g
    on g.id_grado = s.id_grado;;


    -------------------------------------------------------
    USE `db_control_notas`;
CREATE  OR REPLACE VIEW `nota_final` AS
SELECT 
n.id_alumno as ID, d.id_materia as Materia, d.id_bimestre as Bimestre, d.valor_nota as Valor, d.tipo_nota as Nota
FROM
    detalle_nota d
INNER JOIN 
	nota n
    on d.id_nota = n.id_nota;
    ---------------------------------------------------------

    USE `db_control_notas`;
CREATE  OR REPLACE VIEW `ver_asignacion_materia` AS
SELECT 
 m.descripcion as nombre_materia, u.nombre_completo as Nombre_usuario, a.estado

FROM asignacion_materia a
inner join 
materia m
on a.id_materia= m.id_materia
inner join 
usuario u
on a.id_usuario = u.id_usuario;
