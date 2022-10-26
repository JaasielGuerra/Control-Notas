(function () {

    $("#form-usuario").validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            password: {
                required: true,
                maxlength: 100
            },
            repetirPassword: {
                required: true,
                maxlength: 100,
                equalTo: "#password"
            },
        },
        messages: {
            password: {
                required: "La contraseña es requerida",
                maxlength: "La contraseña no debe exceder los 100 caracteres"
            },
            repetirPassword: {
                required: "La contraseña es requerida",
                maxlength: "La contraseña no debe exceder los 100 caracteres",
                equalTo: "Las contraseñas no coinciden"
            },
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            submitFormActualizarContrasenia(form);
        }
    });

    function submitFormActualizarContrasenia(form) {

        loadingBtn($('#form-usuario').find(':submit'));

        let data = formToJSON(form);

        $.ajax({
            type: "PATCH",
            url: "/usuarios/actualizar-contrasenia/" + data.id,
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {

                removeLoadingBtn($('#form-usuario').find(':submit'));

                //si el code es 1
                if (data.code === 1) {

                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.assign('/usuarios/');
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


}());