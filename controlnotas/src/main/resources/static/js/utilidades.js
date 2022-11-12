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
    $(btn).removeClass("is-loading");//quitar efecto carga
}

function loadingSelect(select) {
    $(select).addClass("is-loading");
}

function removeLoadingSelect(select) {
    $(select).removeClass("is-loading");//quitar efecto carga
}

function resetForm(form) {
    let $formAlumno = $(form);
    $formAlumno.get(0).reset()
    $formAlumno.find(".is-danger").not(".button").removeClass("is-danger");
    $formAlumno.find(".is-success").not(".button").removeClass("is-success");
    $formAlumno.find("p.help").remove();
}

//funcion para mostrar mensaje de success
function showMessageSuccess(message) {
    bulmaToast.toast({
        message: message,
        type: 'is-success',
        position: 'top-center',
        duration: 4000,
        dismissible: true,
        animate: {in: 'fadeIn', out: 'fadeOut'},
        pauseOnHover: true,
    });
}

//funcion para mostrar mensaje de error
function showMessageError(message) {
    bulmaToast.toast({
        message: message,
        type: 'is-danger',
        position: 'top-center',
        duration: 8000,
        dismissible: true,
        animate: {in: 'fadeIn', out: 'fadeOut'},
        pauseOnHover: true,
    });
}

//funcion para mostrar mensaje de warning
function showMessageWarning(message) {
    bulmaToast.toast({
        message: message,
        type: 'is-warning',
        position: 'top-center',
        duration: 4000,
        dismissible: true,
        animate: {in: 'fadeIn', out: 'fadeOut'},
        pauseOnHover: true,
    });
}