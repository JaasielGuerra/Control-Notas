//evento cuando se selecciona un grado
$("#grado").change((event) => {

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
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            notif({
                msg: "Error interno en el servidor.",
                type: "error"
            });
        }
    });
});

// validar formulario y  enviar con AJAX
$("#form-nuevo-alumno").validate({

    errorClass: 'is-danger',
    validClass: 'is-success',
    errorElement: 'p',
    messages: {
        codigo: {
            required: "Campo requerido.",
            maxlength: "Máxino 45 caracteres."
        },
        apellido: {
            required: "Campo requerido.",
            maxlength: "Máxino 150 caracteres."
        },
        nombre: {
            required: "Campo requerido.",
            maxlength: "Máxino 150 caracteres."
        },
        direccion: {
            required: "Campo requerido.",
            maxlength: "Máxino 150 caracteres."
        },
        nacimiento: {
            required: "Campo requerido.",
            dateISO: "Formato de fecha inválido."
        },
        seccion: "Seleccione una opción.",
        grado: "Seleccione una opción.",
        observacion: {
            maxlength: "Máxino 150 caracteres.",
            required: "Campo requerido."
        },
    },
    rules: {
        codigo: {
            maxlength: 45
        },
        apellido: {
            maxlength: 150
        },
        nombre: {
            maxlength: 150
        },
        direccion: {
            maxlength: 150
        },
        observacion: {
            maxlength: 150
        },
    },
    highlight: function (element, errorClass, validClass) {
        //cuando la validacion falla, por errorClass (is-danger)
        $(element).addClass(errorClass).removeClass(validClass);
        $("p.is-danger").addClass("help");// agregar clase help a mensajes de error
    },
    submitHandler: function (form) {

        // enviar form con AJAX
        let formJSON = formToJSON(form);
        submitFormAjax(formJSON);

    }
});


function submitFormAjax(formJSON) {

    //poner efecto carga boton submit
    loadingBtn("#btn-submit");

    $.ajax({
        type: "POST",
        url: "/alumno/registrar",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(formJSON),
        success: function (response) {
            notif({
                msg: "¡Alumno registrado con éxito!",
                type: "success"
            });

            //reset form
            resetForm('#form-nuevo-alumno');

            removeLoadingBtn("#btn-submit");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            notif({
                msg: "Error interno en el servidor.",
                type: "error",
                multiline: 1,
            });

            removeLoadingBtn("#btn-submit");
        }
    });
}

