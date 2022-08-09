function formToJSON(form) {
    return $(form).serializeArray().reduce(function (json, {name, value}) {
        json[name] = value;
        return json;
    }, {});
}

function loadingBtn(btn) {
    $(btn).addClass("is-loading");
}

function removeLoadingBtn(btn) {
    $(btn).removeClass("is-loading");//quitar efecto carga boton
}

function resetForm(form) {
    let $formAlumno = $(form);
    $formAlumno.get(0).reset()
    $formAlumno.find(".is-danger").not(".button").removeClass("is-danger");
    $formAlumno.find(".is-success").not(".button").removeClass("is-success");
}