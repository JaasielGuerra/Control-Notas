//funcion wrapper
(function () {
    $('#tbl-alumnos').DataTable({
        lengthMenu: [5, 10, 25, 50], language: {
            lengthMenu: "Mostrar _MENU_ alumnos",
            search: "Buscar en todos los campos: _INPUT_",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ de _END_ de _TOTAL_ alumnos",
        },
        order: [2, "asc"],
    });

})();