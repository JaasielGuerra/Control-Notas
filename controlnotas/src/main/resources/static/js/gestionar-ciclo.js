(function () {


    $('#tbl-historial-bimestres').DataTable({
        lengthMenu: [5, 10, 25, 50], language: {
            lengthMenu: "Mostrar _MENU_ registros",
            emptyTable: "No hay registros para mostrar",
            sZeroRecords: "No se encontron resultados",
            paginate: {
                previous: "Página anterior", next: "Página siguiente"
            },
            info: "Mostrando _START_ al _END_ de _TOTAL_ registros",
            infoEmpty: "Mostrando 0 al 0 de 0 registros",
            infoFiltered: "(filtrado de _MAX_ registros totales)",
            searchPlaceholder: "Buscar en todos los campos",
        },
        order: [],
        dom: 'Blrtip',
        buttons: [
            {
                extend: 'excel',
                text: 'Excel',
                title: 'HISTORIAL DE BIMESTRES',
                sheetName: 'HISTORIAL DE BIMESTRES',
            },
            {
                extend: 'pdf',
                text: 'PDF',
                title: 'HISTORIAL DE BIMESTRES',
                orientation: 'landscape',
                pageSize: 'LETTER',
                filename: 'HISTORIAL DE BIMESTRES',
            }
        ]
    });

    $('#btn-pre-cerrar-ciclo').click(function () {
        openModal('confirmar-ciclo')
    });

    $('#btn-confirmar-cierre-ciclo').click(function () {
        CerrarCiclo($('#input-id-ciclo').val());
    });

    $('#btn-aperturar-ciclo').click(function () {
        AperturarCiclo();
    });

    $('#btn-modal-ciclos-anteriores').click(function () {
        modalCiclosAnteriores();
    });

    $('#btn-aperturar-bimestre').click(function () {
        aperturarBimestre();
    });

    $('#btn-cerrar-bimestre').click(function () {

        let idBimestre = $(this).attr('data-id-bimestre');
        $('#btn-confirmar-cierre-bimestre').attr('data-id-bimestre', idBimestre);
        openModal('confirmar-bimestre');

    });

    $('#btn-confirmar-cierre-bimestre').click(function () {
        let idBimestre = $(this).attr('data-id-bimestre');
        cerrarBimestre(idBimestre);
    });

    $('.btn-pre-activar-bimestre').click(function () {
        let idBimestre = $(this).attr('data-id-bimestre');
        $('#btn-confirmar-activar-bimestre').attr('data-id-bimestre', idBimestre);
        openModal('confirmar-activar-bimestre');
    });

    $('#btn-confirmar-activar-bimestre').click(function () {
        activarBimestre($(this).attr('data-id-bimestre'));
    });

    $('#form-apertura-bimestre').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            descripcion: {
                required: true,
                maxlength: 50
            },
            puntosBase: {
                required: true,
                number: true,
            },
            puntosActividades: {
                required: true,
                number: true,
            },
            puntosActitudinal: {
                required: true,
                number: true,
            },
            puntosEvaluaciones: {
                required: true,
                number: true,
            }
        },
        messages: {
            descripcion: {
                required: "Este campo es requerido",
                maxlength: "El máximo de caracteres es 50"
            },
            puntosBase: {
                required: "Este campo es requerido",
                number: "Este campo debe ser un número",
            },
            puntosActividades: {
                required: "Debe ingresar los puntos de actividades",
                number: "Debe ingresar un número"
            },
            puntosActitudinal: {
                required: "Debe ingresar los puntos de actitudinales",
                number: "Debe ingresar un número"
            },
            puntosEvaluaciones: {
                required: "Debe ingresar los puntos de evaluaciones",
                number: "Debe ingresar un número"
            }
        },
        errorPlacement: function (error, element) {
            error.addClass('help');
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            registrarAperturaBimestre(form);
        }
    });


    $('#form-apertura-ciclo').validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            anio: {
                required: true,
            },
            fechaApertura: {
                required: true,
                date: true,
                dateISO: true,
            },
            diasBaseAsistencia: {
                required: true,
                number: true,
                min: 1,
                max: 200
            }
        },
        messages: {
            anio: {
                required: "Este campo es requerido",
            },
            fechaApertura: {
                required: "Este campo es requerido",
                date: "Debe ingresar una fecha válida",
                dateISO: "Debe ingresar una fecha válida",
            },
            diasBaseAsistencia: {
                required: "Este campo es requerido",
                number: "Este campo debe ser un número",
                min: "El mínimo de días es 1",
                max: "El máximo de días es 200"
            }

        },
        errorPlacement: function (error, element) {
            error.addClass('help');
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            registrarAperturaCiclo(form);
        }
    });


    function aperturarBimestre() {

        $.ajax({
            type: "GET",
            url: `/institucion/nuevo-bimestre`,
            dataType: 'html',
            success: function (data) {

                $('#aperturar-bimestre').find('form').html(data);
                openModal('aperturar-bimestre');

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showMessageError("Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText);
            }
        });

    }

    function registrarAperturaBimestre(form) {

        let formJson = formToJSON(form);

        $.ajax({
            url: '/institucion/aperturar-bimestre',
            type: 'POST',
            data: JSON.stringify(formJson),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.reload();
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
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        });
    }

    function AperturarCiclo() {

        let $selectAniosCiclo = $('#select-anios-ciclo');
        //limpiar el select
        $selectAniosCiclo.empty();

        //llenar select con options de años desde 2000 hasta el año actual, en orden inverso
        let fullYear = new Date().getFullYear();
        for (let i = fullYear; i >= fullYear - 10; i--) {
            $selectAniosCiclo.append($('<option />').val(i).html(i));
        }

        openModal('aperturar-ciclo')
    }

    function registrarAperturaCiclo(form) {

        let formJson = formToJSON(form);

        loadingBtn($('#form-apertura-ciclo').find('button[type="submit"]'));

        $.ajax({
            url: '/institucion/aperturar-ciclo',
            type: 'POST',
            data: JSON.stringify(formJson),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                removeLoadingBtn($('#form-apertura-ciclo').find('button[type="submit"]'));

                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.reload();
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
            error: function (XMLHttpRequest, status, error) {
                showMessageError("Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText);
                removeLoadingBtn($('#form-apertura-ciclo').find('button[type="submit"]'));
            }
        });
    }

    function CerrarCiclo(idCiclo) {

        $.ajax({
            url: '/institucion/cerrar-ciclo/' + idCiclo,
            type: 'PUT',
            data: JSON.stringify({idCiclo: idCiclo}),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {
                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', "El ciclo se ha cerrado correctamente!");
                    location.reload();
                }
            },
            error: function (xhr, status, error) {
                showMessageError("Error " + xhr.status + ", respuesta del servidor: " + xhr.responseText);
            }
        });

    }

    function cerrarBimestre(idBimestre) {
        $.ajax({
            url: '/institucion/cerrar-bimestre/' + idBimestre,
            type: 'PUT',
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', "El bimestre se ha cerrado correctamente!");
                    location.reload();
                }

            },
            error: function (xhr, status, error) {
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        })
    }

    function modalCiclosAnteriores() {

        loadingBtn('#btn-modal-ciclos-anteriores', true);

        $.ajax({
            type: "GET",
            url: `/institucion/ciclos-anteriores`,
            dataType: 'html',
            success: function (data) {

                $('#modal-ciclos').find('#container-tbl-ciclos').html(data);

                //init datatable
                $('#tbl-historial-ciclos').DataTable({
                    lengthMenu: [5, 10, 25, 50], language: {
                        lengthMenu: "Mostrar _MENU_ registros",
                        emptyTable: "No hay registros para mostrar",
                        sZeroRecords: "No se encontron resultados",
                        paginate: {
                            previous: "Página anterior", next: "Página siguiente"
                        },
                        info: "Mostrando _START_ al _END_ de _TOTAL_ registros",
                        infoEmpty: "Mostrando 0 al 0 de 0 registros",
                        infoFiltered: "(filtrado de _MAX_ registros totales)",
                        searchPlaceholder: "Buscar en todos los campos",
                    },
                    order: [],
                    dom: 'Blrtip',
                    buttons: [
                        {
                            extend: 'excel',
                            text: 'Excel',
                            title: 'HISTORIAL DE CICLOS',
                            sheetName: 'HISTORIAL DE CICLOS',
                        },
                        {
                            extend: 'pdf',
                            text: 'PDF',
                            title: 'HISTORIAL DE CICLOS',
                            orientation: 'landscape',
                            pageSize: 'LETTER',
                            filename: 'HISTORIAL DE CICLOS',
                        }
                    ]
                });

                removeLoadingBtn('#btn-modal-ciclos-anteriores');
                openModal('modal-ciclos');
                showMessageSuccess('Historial de ciclos cargados correctamente');

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showMessageError("Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText);
                removeLoadingBtn('#btn-modal-ciclos-anteriores');
            }
        });
    }

    function activarBimestre(idBimestre) {
        $.ajax({
            url: '/institucion/activar-bimestre/' + idBimestre,
            type: 'PATCH',
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {
                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.reload();
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
            error: function (XMLHttpRequest, status, error) {
                showMessageError("Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText);
                removeLoadingBtn($('#form-apertura-ciclo').find('button[type="submit"]'));
            }
        });
    }

})();