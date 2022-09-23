(function (){
    $('#tbl-libros').DataTable({
        lengthMenu: [5, 10, 25, 50], language: {
            lengthMenu: "Mostrar _MENU_ libros",
            search: "Buscar: _INPUT_",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ de _END_ de _TOTAL_ libros",
            infoEmpty: "Mostrando 0 de 0 de 0 registros",
            infoFiltered: "(filtrado de _MAX_ registros totales)",
            searchPlaceholder: "Buscar en todos los campos",
        }

    });
})();