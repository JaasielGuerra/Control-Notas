$('#tbl-alumnos').DataTable({
    lengthMenu: [5, 10, 25, 50], language: {
        lengthMenu: "Mostrar _MENU_ alumnos",
        search: "Buscar: _INPUT_",
        emptyTable: "No hay registros para mostrar",
        sZeroRecords: "No se encontron resultados",
        paginate: {
            previous: "Página anterior", next: "Página siguiente"
        },
        info: "Mostrando _START_ de _END_ de _TOTAL_ alumnos"
    }
});


function verExpediente() {

    //abrir modal
    openModal('modal-expediente');
}

function cambiarGradoAlumno(idAlumno) {

    $.ajax({
        type: "GET",
        url: `/alumno/obtenerAsignacionAlumno?idAlumno=${idAlumno}`,
        dataType: 'json',
        success: function (data) {

            let idSeccion = data["idSeccionAlumno"];
            let $grados = $("#gradoSeccionReasignar");
            $grados.find("option").slice(1).remove();//slice salta el primero item

            for (const i of data["gradoSeccionList"]) {
                //agregar nuevos items
                $grados.append(new Option(`${i['descripcionGrado']} - ${i['descripcionSeccion']}`, i['idSeccion'], false, i['idSeccion'] === idSeccion));
            }

            $('#idAlumnoReasignar').val(idAlumno);
            openModal('modal-grado')

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            notif({
                msg: "Error .", type: "error"
            });
        }
    });

}

function reasignarAlumno() {

    let asignacion = {
        idSeccionAlumno: $('#gradoSeccionReasignar').val(),
        idAlumno: $('#idAlumnoReasignar').val()
    }

    //poner efecto carga boton submit
    loadingBtn("#btn-guardar-reasignacion");

    $.ajax({
        type: "POST",
        url: "/alumno/cambiarAsignacion",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(asignacion),
        success: function (response) {
            notif({
                msg: "¡Alumno reasignado con éxito!",
                type: "success"
            });

            removeLoadingBtn("#btn-guardar-reasignacion");
            window.location = "/alumno/consultar";
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            notif({
                msg: "Error interno en el servidor.",
                type: "error",
                multiline: 1,
            });

            removeLoadingBtn("#btn-guardar-reasignacion");
        }
    });
}

function preEliminarAlumno(idAlumno) {

    $('#id-alumno-eliminar').val(idAlumno);
    openModal('modal-confirmacion-eliminar');
}