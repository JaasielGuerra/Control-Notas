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

    //evento preeliminar para el boton de eliminar
    $('#tbl-listado').on('click', '.btn-pre-eliminar', function () {
        let id = $(this).attr('data-id-listado');
        $('#modal-confirmacion-eliminar').find('#btn-confirmar').attr('data-id-listado', id);
        openModal('modal-confirmacion-eliminar');
    });

    //evento para el boton de eliminar
    $('#modal-confirmacion-eliminar').on('click', '#btn-confirmar', function () {
        let id = $(this).attr('data-id-listado');

        $.ajax({
            url: '/asistencia/eliminar-listado/' + id,
            type: 'DELETE',
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {

                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.reload();
                }
                //si el code es 0
                else if (data.code === 0) {
                    showMessageError(data.message);
                    //recorrer los errores, concatenarlos
                    let errores = '';
                    $.each(data.errors, function (i, error) {
                        errores += error + '<br>';
                    });
                    //mostrar errores
                    showMessageError(errores);
                }

            },
            error: function (xhr, status, error) {
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        });
    });


})();