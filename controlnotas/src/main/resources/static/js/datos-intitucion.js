//function wrapper
(function () {

    function guardarDatosInst(form) {

        let datosInstitucion = formToJSON(form);
        let idInstitucion = $('#id-institucion').val();

        $.ajax({
            url: '/institucion/' + idInstitucion,
            type: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(datosInstitucion),
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

    // validar el formulario
    $('#form-institucion').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            codigo: {
                required: true,
                minlength: 10,
                maxlength: 13
            },
            nombre: {
                required: true,
                minlength: 10,
                maxlength: 100
            },
            direccion: {
                minlength: 10,
                maxlength: 80
            },
            correo: {
                email: true,
                minlength: 10,
                maxlength: 50
            },
            telefono: {
                minlength: 8,
                maxlength: 8
            },
            nivel: {
                minlength: 5,
                maxlength: 15
            },
            sector: {
                minlength: 5,
                maxlength: 15
            },
            nombreDirector: {
                required: true,
                minlength: 3,
                maxlength: 50
            },
            jornada: {
                minlength: 5,
                maxlength: 15
            }
        },
        messages: {
            codigo: {
                required: "El código es obligatorio",
                minlength: "El código debe tener al menos 10 caracteres",
                maxlength: "El código debe tener máximo 13 caracteres"
            },
            nombre: {
                required: "El nombre es obligatorio",
                minlength: "El nombre debe tener al menos 10 caracteres",
                maxlength: "El nombre debe tener máximo 100 caracteres"
            },
            direccion: {
                minlength: "La dirección debe tener al menos 10 caracteres",
                maxlength: "La dirección debe tener máximo 80 caracteres"
            },
            correo: {
                email: "El correo no es válido, el formato debe ser name@domain.com",
                minlength: "El correo debe tener al menos 10 caracteres",
                maxlength: "El correo debe tener máximo 50 caracteres"
            },
            telefono: {
                minlength: "El teléfono debe tener al menos 8 caracteres",
                maxlength: "El teléfono debe tener máximo 8 caracteres"
            },
            nivel: {
                minlength: "El nivel debe tener al menos 5 caracteres",
                maxlength: "El nivel debe tener máximo 15 caracteres"
            },
            sector: {
                minlength: "El sector debe tener al menos 5 caracteres",
                maxlength: "El sector debe tener máximo 15 caracteres"
            },
            nombreDirector: {
                required: "El nombre del director es obligatorio",
                minlength: "El nombre del director debe tener al menos 3 caracteres",
                maxlength: "El nombre del director debe tener máximo 50 caracteres"
            },
            jornada: {
                minlength: "La jornada debe tener al menos 5 caracteres",
                maxlength: "La jornada debe tener máximo 15 caracteres"
            }

        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            // aqui va el codigo para guardar los datos
            guardarDatosInst(form);
        }
    });



})();