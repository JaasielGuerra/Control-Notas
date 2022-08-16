$('#tbl-alumnos').DataTable({
    lengthMenu: [5, 10, 25, 50],
    language: {
        lengthMenu: "Mostrar _MENU_ alumnos",
        search: "Buscar: _INPUT_",
        emptyTable: "No hay registros para mostrar",
        sZeroRecords: "No se encontron resultados",
        paginate: {
            previous: "Página anterior",
            next: "Página siguiente"
        },
        info: "Mostrando _START_ de _END_ de _TOTAL_ alumnos"
    }
});


function verExpediente() {

    //abrir modal
    openModal('modal-expediente');
}

function cambiarGradoAlumno() {

    openModal('modal-grado')
}

function preEliminarAlumno(idAlumno) {

    $('#id-alumno-eliminar').val(idAlumno);
    openModal('modal-confirmacion-eliminar');
}