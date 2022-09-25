//funcion wrapper
(function () {

    //evento para listar alumnos
    $('.btn-listar-alumnos').on('click', function () {
        let idGrado = $(this).attr('data-id-grado');
        let descripcionGrado = $(this).attr('data-descripcion-grado');
        $('#title-listado-alumnos').text('Listado de alumnos del grado ' + descripcionGrado);
        loadingBtn($(this));
        cargarTablaListadoAlumno(idGrado);
    });

    //funcion para cargar la tabla de listado de alumnos
    function cargarTablaListadoAlumno(idGrado) {

        $.ajax({
            url: '/control-lectura/listar-alumnos',
            type: 'GET',
            data: {idGrado: idGrado},
            dataType: 'html',
            success: function (data) {

                $('#container-list-alumnos').html(data);
                showMessageSuccess('Se cargó el listado de alumnos');
                removeLoadingBtn($('.btn-listar-alumnos'));

                //inicializar datatable
                $('#tbl-alumnos-lectura').DataTable({
                    lengthMenu: [10, 25, 50], language: {
                        lengthMenu: "Mostrar _MENU_ alumnos",
                        search: "Buscar: _INPUT_",
                        emptyTable: "No hay registros para mostrar",
                        sZeroRecords: "No se encontron resultados",
                        paginate: {
                            previous: "Página anterior", next: "Página siguiente"
                        },
                        info: "Mostrando _START_ al _END_ de _TOTAL_ alumnos",
                        infoEmpty: "Mostrando 0 de 0 de 0 registros",
                        infoFiltered: "(filtrado de _MAX_ registros totales)",
                        searchPlaceholder: "Buscar alumno",
                    }
                });

                //inicializar evento para abrir modal de registro de lectura
                $('#tbl-alumnos-lectura').on('click', '.btn-registro-lectura', function () {
                    let idAlumno = $(this).attr('data-id-alumno');
                    loadingBtn($(this));
                    modalRegistroLecturaAlumno(idAlumno);
                });

            },
            error: function (xhr, status, error) {
                showMessageError('Ocurrió un error al cargar el listado de alumnos ' + xhr.responseText);
                removeLoadingBtn($('.btn-listar-alumnos'));
            }
        });
    }

    function modalRegistroLecturaAlumno(idAlumno) {

        $.ajax({
            url: '/control-lectura/obtener-alumno/' + idAlumno,
            type: 'GET',
            dataType: 'html',
            success: function (data) {

                $('#form-modal-registro-lectura').html(data);

                //inicializar datatable
                $('#tbl-detalle-lectura').DataTable({
                    lengthMenu: [5, 10, 25, 50], language: {
                        lengthMenu: "Mostrar _MENU_ detalles",
                        search: "Buscar: _INPUT_",
                        emptyTable: "No hay registros para mostrar",
                        sZeroRecords: "No se encontron resultados",
                        paginate: {
                            previous: "Página anterior", next: "Página siguiente"
                        },
                        info: "Mostrando _START_ al _END_ de _TOTAL_ registros",
                        infoEmpty: "Mostrando 0 de 0 de 0 registros",
                        infoFiltered: "(filtrado de _MAX_ registros totales)",
                        searchPlaceholder: "Buscar detalle",
                    },
                    order: []
                });

                //inicializar validacion de campos
                let validator = $( "#form-modal-registro-lectura" ).validate({
                    errorClass: 'is-danger',
                    validClass: 'is-success',
                    errorElement: 'p',
                    rules: {
                        paginasLeidas: {
                            required: true,
                            number: true,
                            min: 1,
                            max: 100
                        },
                        idLibroNuevo: {
                            required: true
                        }
                    },
                    messages: {
                        paginasLeidas: {
                            required: "Ingrese un valor",
                            number: "Debe ser un número",
                            min: "El valor mínimo es 1",
                            max: "El valor máximo es 100"
                        },
                        idLibroNuevo: {
                            required: "Seleccione un libro"
                        }
                    },
                    errorPlacement: function (error, element) {
                        // Add the `help` class to the error element
                        error.addClass("help");
                        error.insertAfter(element);
                    }
                });

                //evento terminar lectura libro
                $('#btn-terminar-lectura').on('click', function () {

                    let idAlumno = $('#idAlumno').val();
                    let idLibroActual = $('#idLibroActual').val();

                    registrarOperacionLectura(
                        3,
                        idAlumno,
                        idLibroActual,
                        0
                    );
                });

                //evento asignar nuevo libro
                $('#btn-asignar-libro').on('click', function () {

                    //validar que seleccione un libro
                    if (!validator.element( "#idLibroNuevo" )) {
                        return;
                    }

                    let idAlumno = $('#idAlumno').val();
                    let idLibroNuevo = $('#idLibroNuevo').val();

                    registrarOperacionLectura(
                        1,
                        idAlumno,
                        idLibroNuevo,
                        0
                    );
                });

                //evento registrar avance lectura
                $('#btn-registrar-avance').on('click', function () {

                    //validar que sea numero
                    if (!validator.element( "#paginasLeidas" )) {
                        return;
                    }

                    let paginasLeidas = $('#paginasLeidas').val();
                    let idAlumno = $('#idAlumno').val();
                    let idLibroActual = $('#idLibroActual').val();

                    registrarOperacionLectura(
                        2,
                        idAlumno,
                        idLibroActual,
                        paginasLeidas
                    );
                });


                removeLoadingBtn($('.btn-registro-lectura'));
                openModal('registro-lectura');
            },
            error: function (xhr, status, error) {
                showMessageError('Ocurrió un error al cargar el modal de registro de lectura ' + xhr.responseText);
            }
        });
    }

    function registrarOperacionLectura(operacion, idAlumno, idLibroActual, paginasLeidas) {

        $.ajax({
            type: "POST",
            url: "/control-lectura/registrar-operacion",
            data: JSON.stringify({
                operacion: operacion,
                idLibro: idLibroActual,
                idAlumno: idAlumno,
                paginasLeidas: paginasLeidas
            }),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                //si el code es 1
                if (data.code === 1) {

                    showMessageSuccess(data.message);
                    modalRegistroLecturaAlumno(idAlumno);

                    //obtener id grado del listado de alumnos de la tabla donde se muestran los alunmos para lectura
                    let idGrado = $('#id-grado-alumnos').val();
                    cargarTablaListadoAlumno(idGrado);

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

})();

