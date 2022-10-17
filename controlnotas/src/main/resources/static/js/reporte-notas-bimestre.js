//funcion wrapper
(function () {
    $("#form-parametros-reporte").validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            codAlumno: {
                required: true
            },
            ciclo: {
                required: true
            },
        },
        messages: {
            codAlumno: {
                required: "Digite código del alumno"
            },
            ciclo: {
                required: "Seleccione el ciclo"
            },
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            form.submit();
        }
    });

    // imprimir reporte
    $("#btn-print").click(function () {
        $("#print-report").printMe(
            {
                "path": ["/css/bulma.min.css"],
                "title": ""
            }
        );
    });
})();