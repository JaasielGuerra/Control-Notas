(function () {


    $('#btn-aperturar-bimestre').click(function () {
        aperturarBimestre();
    });

    $('#btn-cerrar-bimestre').click(function () {

        let idBimestre = $(this).attr('data-id-bimestre');
        $('#btn-confirmar-cierre-bimestre').attr('data-id-bimestre', idBimestre);
        openModal('confirmar-bimestre');

    });

    $('#btn-confirmar-cierre-bimestre').click(function () {
        let idBimestre = $(this).attr('data-id-bimestre');
        cerrarBimestre(idBimestre);
    });

    $('#form-apertura-bimestre').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            descripcion: {
                required: true,
                maxlength: 50
            },
            puntosBase: {
                required: true,
                number: true,
            },
            puntosActividades: {
                required: true,
                number: true,
            },
            puntosActitudinal: {
                required: true,
                number: true,
            },
            puntosEvaluaciones: {
                required: true,
                number: true,
            }
        },
        messages: {
            descripcion: {
                required: "Este campo es requerido",
                maxlength: "El máximo de caracteres es 50"
            },
            puntosBase: {
                required: "Este campo es requerido",
                number: "Este campo debe ser un número",
            },
            puntosActividades: {
                required: "Debe ingresar los puntos de actividades",
                number: "Debe ingresar un número"
            },
            puntosActitudinal: {
                required: "Debe ingresar los puntos de actitudinales",
                number: "Debe ingresar un número"
            },
            puntosEvaluaciones: {
                required: "Debe ingresar los puntos de evaluaciones",
                number: "Debe ingresar un número"
            }
        },
        errorPlacement: function (error, element) {
            error.addClass('help');
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            registrarAperturaBimestre(form);
        }
    });


    function aperturarBimestre() {

        $.ajax({
            type: "GET",
            url: `/institucion/nuevo-bimestre`,
            dataType: 'html',
            success: function (data) {

                $('#aperturar-bimestre').find('form').html(data);
                openModal('aperturar-bimestre');
                showMessageSuccess('Se ha cargado la rúbrica del último bimestre');

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showMessageError("Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText);
            }
        });

    }

    function registrarAperturaBimestre(form) {

        let formJson = formToJSON(form);

        $.ajax({
            url: '/institucion/aperturar-bimestre',
            type: 'POST',
            data: JSON.stringify(formJson),
            contentType: 'application/json',
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

    function AperturarCiclo() {
        openModal('aperturar-ciclo')
    }

    function CerrarCiclo() {
        openModal('confirmar-ciclo')
    }

    function cerrarBimestre(idBimestre) {
        $.ajax({
            url: '/institucion/cerrar-bimestre/' + idBimestre,
            type: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                    //si el code es 1
                    if (data.code === 1) {
                        //guardar mensaje de exito en el local storage
                        localStorage.setItem('messageSuccess', "El bimestre se ha cerrado correctamente!");
                        location.reload();
                    }

            },
            error: function (xhr, status, error) {
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        })
    }

    function CicloAnterior() {
        openModal('ciclo-anterior')
    }

})();