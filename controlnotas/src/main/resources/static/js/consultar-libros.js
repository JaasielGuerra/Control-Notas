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

    //llama al modal de confirmacion de eliminacion
    $('#tbl-libros').on('click', '.btn-pre-eliminar', function (){
        let id = $(this).attr('data-id-libro');
        $('#btn-confirmacion-eliminar-libro').attr('data-id-libro', id);
        openModal('modal-eliminar-libro');
    });

    //elimina el libro
    $('#modal-eliminar-libro').on('click', '#btn-confirmacion-eliminar-libro', function (){
        EliminarLibro($(this).attr('data-id-libro'));
    });

    function EliminarLibro(id){
        $.ajax({
            url: '/consultar-libros/eliminar/' + id,
            type: 'DELETE',
            dataType: 'json',
            success: function (data){
                if (data.code === 1){
                    localStorage.setItem('messageSuccess', data.message);
                    location.reload();
                }
                else if (data.code === 0){
                    showMessageError(data.message);

                    let errores = '';
                    $.each(data.errors, function (i, error){
                        errores += error + '<br>';
                    });
                    showMessageError(errores)
                }
            },
            error: function (xhr, status, error){
                showMessageError('Se produjo un error en el servidor' + xhr.responseText);
            }
        });
    }

})();