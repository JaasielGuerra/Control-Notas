//funcion wrapper
(function () {

    $('#form-editar-evaluacion').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            descripcion: {
                required: true,
                minlength: 5,
                maxlength: 100
            },
            fecha: {
                required: true,
                date: true,
                dateISO: true
            },
            ponderacion: {
                required: true,
                number: true,
                min: 0,
                max: 100
            },
            idTipoEvaluacionId: {
                required: true
            },
            idMateriaId: {
                required: true
            }
        },
        messages: {
            descripcion: {
                required: "Campo requerido.",
                minlength: "Mínimo 5 caracteres.",
                maxlength: "Máximo 100 caracteres."

            },
            fecha: {
                required: "Campo requerido.",
                date: "Formato de fecha inválido.",
                dateISO: "Formato de fecha inválido."

            },
            ponderacion: {
                required: "Campo requerido.",
                number: "Solo números.",
                min: "Mínimo 0.",
                max: "Máximo 100."
            },
            idTipoEvaluacionId: {
                required: "Seleccione una opción."

            },
            idMateriaId: {
                required: "Seleccione una opción."
            }
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            // aqui va el codigo para guardar los datos
            submitFormEditarEvaluacion(form);
        }
    });

    function submitFormEditarEvaluacion(form) {

        let data = formToJSON(form);
        let id = $('#id-evaluacion').val();

        $.ajax({
            type: "PUT",
            url: "/evaluacion/actualizar/" + id,
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                //si el code es 1
                if (data.code === 1) {

                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', 'Éxito: ' + data.message);
                    location.assign('/evaluacion/consultar');
                }
                //si el code es 0
                else if (data.code === 0) {
                    showMessageError('Fatal: '+ data.message);
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