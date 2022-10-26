(function () {
    $('#tbl-usuarios').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ registros por página",
            "zeroRecords": "No se encontraron registros",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay registros disponibles",
            "infoFiltered": "(filtrado de _TOTAL_ registros totales)",
            "search": "Buscar:",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        },
        "order": []
    });

    $('.btn-activar').on('click', function () {
        $('#btn-confirmar-activar').attr('data-id', $(this).closest('tr').find('td').eq(0).text());
        $('#modal-activar').addClass('is-active');
    });

    $('.btn-desactivar').on('click', function () {
        $('#btn-confirmar-desactivar').attr('data-id', $(this).closest('tr').find('td').eq(0).text());
        $('#modal-desactivar').addClass('is-active');
    });

    $('#btn-confirmar-activar').on('click', function () {
        let id = $(this).attr('data-id');
        activarUsuario(id);
    });

    $('#btn-confirmar-desactivar').on('click', function () {
        let id = $(this).attr('data-id');
        desactivarUsuario(id);
    });

    function desactivarUsuario(id) {
        $.ajax({
            url: '/usuarios/' + id,
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

    function activarUsuario(id) {
        $.ajax({
            url: '/usuarios/' + id,
            type: 'PUT',
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

})();
