//funcion wrapper
(function () {

    $('#btn-modal-ayuda').click(function () {
        openModal('modal-ayuda-tipo');
    });

    //evento cuando se selecciona un grado
    $("#grado").change((event) => {

        loadingSelect($('#grado').parent());
        let idGrado = $("#grado").val();

        $.ajax({
            type: "GET",
            url: `/consultas/secciones?idGrado=${idGrado}`,
            dataType: 'json',
            success: function (data) {

                let $seccion = $("#seccion");
                $seccion.find("option").slice(1).remove();//slice para saltar primer item

                for (const i of data) {
                    $seccion.append(new Option(i["descripcion"], i["id"]));
                }

                removeLoadingSelect($('#grado').parent());
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                removeLoadingSelect($('#grado').parent());
                showMessageError('Se produjo un error en el servidor ' + XMLHttpRequest.responseText);
            }
        });
    });

    //validar formulario
    $("#form-nuevo-listado").validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            observacion: {
                required: false,
                maxlength: 100,
            },
            fecha: {
                required: true,
                date: true,
                dateISO: true,
            },
            tipo: {
                required: true,
            },
            grado: {
                required: true,
            },
            idSeccion: {
                required: true,
            }
        },
        messages: {
            observacion: {
                required: "Este campo es requerido.",
                maxlength: "Este campo debe tener máximo 100 caracteres.",
            },
            fecha: {
                required: "Este campo es requerido.",
                date: "Este campo debe ser una fecha válida.",
                dateISO: "Este campo debe ser una fecha válida.",
            },
            tipo: {
                required: "Seleccione una opción.",
            },
            grado: {
                required: "Seleccione una opción.",
            },
            idSeccion: {
                required: "Seleccione una opción.",
            }
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            registrarNuevoListado(form);
        }
    });

    //enviar el formulario con AJAX
    function registrarNuevoListado(form) {



        let formulario = formToJSON(form);

        $.ajax({
            type: "POST",
            url: "/asistencia/registrar-listado",
            data: JSON.stringify(formulario),
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.assign('/asistencia/' + data.data.id);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showMessageError('Se produjo un error en el servidor ' + XMLHttpRequest.responseText);
            }
        })

    }


})();