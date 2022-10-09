//function wrapper
(function () {

    $('#tbl-listado').DataTable({
        lengthMenu: [10, 25, 50], language: {
            lengthMenu: "Mostrar _MENU_ registros",
            search: "Buscar: _INPUT_",
            searchPlaceholder: "Buscar en todos los campos",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ al _END_ de _TOTAL_ registros",
        },
        order: [],
    });


})();