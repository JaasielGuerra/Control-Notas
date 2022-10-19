//function wrapper
(function () {
    $('#tbl-alumnos').DataTable({
        lengthMenu: [25], language: {
            lengthMenu: "Mostrar _MENU_ alumnos",
            search: "Buscar en todos los campos: _INPUT_",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ al _END_ de _TOTAL_ alumnos",
        },
        order: [1, "asc"],
        dom: 'Brtip',
        buttons: [
            {
                extend: 'excel',
                text: '<i class="fas fa-file-excel mr-1"></i>Exportar a Excel',
                title: 'REPORTE DE ALUMNOS',
                sheetName: 'ALUMNOS',
                className: 'button is-success',
            },
            {
                extend: 'print',
                text: '<i class="fas fa-print mr-1"></i>Imprimir',
                className: 'button is-info',
                title: '',
                customize: function (win) {

                    var last = null;
                    var current = null;
                    var bod = [];

                    var css = '@page { size: landscape; }',
                        head = win.document.head || win.document.getElementsByTagName('head')[0],
                        style = win.document.createElement('style');

                    style.type = 'text/css';
                    style.media = 'print';

                    if (style.styleSheet) {
                        style.styleSheet.cssText = css;
                    } else {
                        style.appendChild(win.document.createTextNode(css));
                    }

                    head.appendChild(style);


                    $(win.document.body).prepend(
                        $('#encabezado').html()
                    );

                    $(win.document.body).find('table')
                        .addClass('compact')
                        .css('font-size', 'inherit');
                }
            }
        ]
    });
})();