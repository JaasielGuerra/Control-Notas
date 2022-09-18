//funcion wrapper
(function () {

    $('#tbl-evaluaciones').DataTable({
        lengthMenu: [5, 10, 25, 50], language: {
            lengthMenu: "Mostrar _MENU_ evaluaciones",
            search: "Buscar: _INPUT_",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ de _END_ de _TOTAL_ evaluaciones",
            infoEmpty: "Mostrando 0 de 0 de 0 registros",
            infoFiltered: "(filtrado de _MAX_ registros totales)",
            searchPlaceholder: "Buscar en todos los campos",
        }
    });


    $('#tbl-evaluaciones').on('click', '.btn-eliminar', function () {
        let id = $(this).attr('data-id-eval');
        preEliminarEvaluacion(id);
    });

    $('#modal-eliminar-eval').on('click', '#btn-confirmar-eliminacion', function () {
        let id = $(this).attr('data-id-eval');
        eliminarEvaluacion(id);
    });


    /**
     * Función que muestra el modal de confirmación de eliminación
     * @param id
     */
    function preEliminarEvaluacion(id) {
        $('#btn-confirmar-eliminacion').attr('data-id-eval', id);
        openModal('modal-eliminar-eval')
    }

    /**
     * Función que elimina la evaluación
     * @param id
     */
    function eliminarEvaluacion(id) {
        $.ajax({
            url: '/evaluacion/eliminar/' + id,
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
    }


    //cuando se carge el documento verificar si hay un mensaje de exito en el local storage y mostrarlo
    $(document).ready(function () {
        let messageSuccess = localStorage.getItem('messageSuccess');
        if (messageSuccess) {
            showMessageSuccess(messageSuccess);
            localStorage.removeItem('messageSuccess');
        }
    });

})();