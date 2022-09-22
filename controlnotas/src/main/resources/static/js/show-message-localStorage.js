//cuando se carge el documento verificar si hay un mensaje de exito en el local storage y mostrarlo
$(document).ready(function () {
    let messageSuccess = localStorage.getItem('messageSuccess');
    if (messageSuccess) {
        showMessageSuccess(messageSuccess);
        localStorage.removeItem('messageSuccess');
    }
});