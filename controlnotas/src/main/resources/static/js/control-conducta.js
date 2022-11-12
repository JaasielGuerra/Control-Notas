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
            info: "Mostrando _START_ al _END_ de _TOTAL_ alumnos en total",
        },
        order: [2, "asc"],
    });

    /**
     * levantar modal para realizar calificacion de conducta
     */
    $('.btn-conducta').click(function () {
        //get id data split by comma
        const idAlumno = $(this).data('id').split(',')[0];
        const idGrado = $(this).data('id').split(',')[1];

        //set id alumno en modal
        $('#form-calificar-conducta').find("#id-alumno").val(idAlumno);

        $.ajax({
            url: '/consultas/materias?grado=' + idGrado,
            type: 'GET',
            dataType: 'json',
            success: function (data) {

                cargarMaterias(data);
                resetForm("#form-calificar-conducta");
                openModal('modal-conducta');

            }, error: function (xhr, status) {
                showMessageError('Error al cargar las materias ' + xhr.responseText);
            }
        });

        // cargar materias al select materia
        function cargarMaterias(data) {

            //recorrer materias con each y agregarlas al select con id materias
            let $materia = $('#form-calificar-conducta').find('#materias');
            $materia.empty();
            $.each(data, function (i, item) {
                $materia.append($('<option>', {
                    value: item.id, text: item.descripcion
                }));
            });
        }

    });


    $("#form-calificar-conducta").validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            idMateria: {
                required: true
            },
            descripcion: {
                required: true,
                maxlength: 100,
            },
            fecha: {
                required: true,
                date: true,
                dateISO: true
            },
            operacion: {
                required: true
            },
            puntos: {
                required: true,
                number: true,
                min: 0,
                max: 10
            }
        },
        messages: {
            idMateria: {
                required: 'Seleccione una materia'
            },
            descripcion: {
                required: 'Ingrese una descripción',
                maxlength: 'La descripción no debe exceder los 100 caracteres'
            },
            fecha: {
                required: 'Ingrese una fecha',
                date: 'Ingrese una fecha válida',
                dateISO: 'Ingrese una fecha válida'
            },
            operacion: {
                required: 'Seleccione una operación'
            },
            puntos: {
                required: 'Ingrese una cantidad de puntos',
                number: 'Ingrese una cantidad válida',
                min: 'La cantidad mínima es 0',
                max: 'La cantidad máxima es 10'
            }
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            submitCalificacionConducta(form);
        }
    });

    // enviar formulario por ajax
    function submitCalificacionConducta(form) {

        loadingBtn($('#form-calificar-conducta').find(':submit'));

        // serializar datos a json
        let data = formToJSON(form);

        $.ajax({
            type: "POST",
            url: "/conducta/calificar",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {

                removeLoadingBtn($('#form-calificar-conducta').find(':submit'));

                //si el code es 1
                if (data.code === 1) {

                    //guardar mensaje de exito en el local storage
                    showMessageSuccess(data.message);
                    resetForm("#form-calificar-conducta");
                    closeModal(document.getElementById('modal-conducta'));
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
                removeLoadingBtn($('#form-usuario').find(':submit'));
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        });
    }


    $('.btn-historial').click(function () {
        openModal('modal-historial');
    });


})();