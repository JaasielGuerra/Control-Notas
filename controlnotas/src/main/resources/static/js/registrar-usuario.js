
(function () {

    //arreglo de asignaciones
    let asignaciones = [];

    $("#form-usuario").validate({
        errorClass: 'is-danger',
        validClass: 'is-success',
        errorElement: 'p',
        rules: {
            nombreCompleto: {
                required: true,
                maxlength: 100
            },
            user: {
                required: true,
                maxlength: 45
            },
            password: {
                required: true,
                maxlength: 100
            },
            repetirPassword: {
                required: true,
                maxlength: 100,
                equalTo: "#password"
            },
            idRol: {
                required: true
            },
        },
        messages: {
            nombreCompleto: {
                required: "El nombre completo es requerido",
                maxlength: "El nombre completo no debe exceder los 100 caracteres"
            },
            user: {
                required: "El usuario es requerido",
                maxlength: "El usuario no debe exceder los 45 caracteres"
            },
            password: {
                required: "La contraseña es requerida",
                maxlength: "La contraseña no debe exceder los 100 caracteres"
            },
            repetirPassword: {
                required: "La contraseña es requerida",
                maxlength: "La contraseña no debe exceder los 100 caracteres",
                equalTo: "Las contraseñas no coinciden"
            },
            idRol: {
                required: "El rol es requerido"
            },
        },
        errorPlacement: function (error, element) {
            // Add the `help` class to the error element
            error.addClass("help");
            error.insertAfter(element);
        },
        submitHandler: function (form) {
            submitFormRegistrarUsuarion(form);
        }
    });

    function submitFormRegistrarUsuarion(form) {

        loadingBtn($('#form-usuario').find(':submit'));

        let data = formToJSON(form);
        data.asignaciones = asignaciones;

        $.ajax({
            type: "POST",
            url: "/usuarios/",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {

                removeLoadingBtn($('#form-usuario').find(':submit'));

                //si el code es 1
                if (data.code === 1) {

                    //guardar mensaje de exito en el local storage
                    localStorage.setItem('messageSuccess', data.message);
                    location.assign('/usuarios/');
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
                removeLoadingBtn($('#form-usuario').find(':submit'));
                showMessageError('Se produjo un error en el servidor ' + xhr.responseText);
            }
        });
    }

    //evento cuando se selecciona un grado
    $("#gradoSeccion").change((event) => {
        $.ajax({
            url: '/consultas/materias?seccion=' + event.target.value,
            type: 'GET',
            dataType: 'json',
            success: function (data) {

                cargarMaterias(data);

            }, error: function (xhr, status) {
                showMessageError('Error al cargar las materias ' + xhr.responseText);
            }
        });

        // cargar materias al select materia
        function cargarMaterias(data) {

            //recorrer materias con each y agregarlas al select con id materias
            let $materia = $('#materia');
            $materia.empty();
            $materia.removeAttr('disabled');
            $('#btn-asignar').removeAttr('disabled');
            $.each(data, function (i, item) {
                $materia.append($('<option>', {
                    value: item.id, text: item.descripcion
                }));
            });
        }
    });

    //agregar una asignacion
    $("#btn-asignar").click((event) => {
        let idSeccion = Number($("#gradoSeccion").val());
        let gradoSeccion = $("#gradoSeccion option:selected").text();
        let idMateria =  Number($("#materia").val());
        let materia = $("#materia option:selected").text();

        //validar que no se repita la asignacion
        let existe = false;
        $.each(asignaciones, function (i, item) {
            if (item.idSeccion === idSeccion && item.idMateria === idMateria) {
                existe = true;
                return false;
            }
        });

        if (existe) {
            showMessageWarning('La asignación ya existe');
            return;
        }

        //agregar asignacion al arreglo
        asignaciones.push({
            idSeccion: idSeccion,
            gradoSeccion: gradoSeccion,
            idMateria: idMateria,
            materia: materia
        });

        //ocutal el mensaje de que no hay asignaciones
        $("#sin-asignaciones").addClass('is-hidden');

        //agregar asignacion a la tabla
        let $table = $('#tabla-asignaciones');
        $table.append($('<tr>', {
            id: 'asignacion-' + idSeccion + '-' + idMateria,
            html: '<td>' + gradoSeccion + '</td>' +
                '<td>' + materia + '</td>' +
                '<td><button class="button is-danger is-small" type="button" onclick="eliminarAsignacion(' + idSeccion + ',' + idMateria + ')"><i class="fas fa-trash-alt mr-1"></i>Eliminar</button></td>'
        }));

    });

    //eliminar asignacion
    window.eliminarAsignacion = function (idSeccion, idMateria) {
        //eliminar asignacion del arreglo
        $.each(asignaciones, function (i, item) {
            if (item.idSeccion === idSeccion && item.idMateria === idMateria) {
                asignaciones.splice(i, 1);
                return false;
            }
        });

        //eliminar asignacion de la tabla
        $('#asignacion-' + idSeccion + '-' + idMateria).remove();

        //si el arreglo esta vacio, poner el mensaje de que no hay asignaciones
        if (asignaciones.length === 0) {
            $("#sin-asignaciones").removeClass('is-hidden');
        }
    }

}());

