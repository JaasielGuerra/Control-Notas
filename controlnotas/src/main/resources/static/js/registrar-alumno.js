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
            bulmaToast.toast({
                message: "Error interno en el servidor.",
                type: "is-danger",
                position: 'top-center',
                dismissible: true,
                duration: 4000,
                pauseOnHover: true,
                animate: { in: 'fadeIn', out: 'fadeOut' },
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
        encargado: {
            required: "Seleccione una opción.",
        }
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
        encargado: {
            required: true
        }
    },
    errorPlacement: function (error, element) {
        // Add the `help` class to the error element
        error.addClass("help");
        error.insertAfter(element);
    },
    submitHandler: function (form) {

        // enviar form con AJAX
        let formJSON = formToJSON(form);

        let formChecklistJSON = formToJSON('#modal-checklist-expediente');
        formJSON["plantillaChecklists"] = llenarPlantillaChecklist(formChecklistJSON);

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
            bulmaToast.toast({
                message: "¡Alumno registrado con éxito!",
                type: "is-success",
                position: 'top-center',
                dismissible: true,
                duration: 4000,
                pauseOnHover: true,
                animate: { in: 'fadeIn', out: 'fadeOut' },
            });

            //reset form
            resetForm('#form-nuevo-alumno');

            removeLoadingBtn("#btn-submit");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            bulmaToast.toast({
                message: "Error interno en el servidor.",
                type: "is-danger",
                position: 'top-center',
                dismissible: true,
                duration: 4000,
                pauseOnHover: true,
                animate: { in: 'fadeIn', out: 'fadeOut' },
            });

            removeLoadingBtn("#btn-submit");
        }
    });
}

// funcion para llenar plantillaChecklists
function llenarPlantillaChecklist(formJSON) {
    let plantilla = [];//limpiar array
    Object.keys(formJSON).forEach(function (key) {
        plantilla.push({idDocumentoExpediente: key, estado: formJSON[key]});
    });
    return plantilla;
}
