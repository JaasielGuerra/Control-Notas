(function () {
    $('#form-editar-libro').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',

        rules: {
            nombre: {
                required: true,
                minlength: 5,
                maxlength: 100
            },
            descripcion: {
                required: true,
                minlength: 5,
                maxlength: 100
            }
        },
        messages: {
            nombre: {
                required: "Campo requerido",
                minlength: "Mínimo 5 caracteres.",
                maxlength: "Maximo 100 caracteres."
            },
            descripcion: {
                required: "Campo requerido",
                minlength: "Mínimo 5 caracteres.",
                maxlength: "Maximo 100 caracteres."
            }
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form){
            submitFormActualizarLibro(form)
        }

    });


    // Función para enviar el formulario de actualización de libro
    function submitFormActualizarLibro(form){

        let data = formToJSON(form);
        let id = $('#id-libro').val();

        $.ajax( {
            type: "PUT",
            url: "/libros/actualizar/" + id,
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            success: function (data){
                if (data.code === 1){
                    localStorage.setItem('messageSuccess', data.message);
                    location.assign("/libros");
                }
                else if(data.code===0){
                    showMessageError(data.message);
                    let errores = '';
                    $.each(data.errors, function (i,error){
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