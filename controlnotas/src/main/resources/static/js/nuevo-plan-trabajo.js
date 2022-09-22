/**
 * Funcionalidades para la carga de nuevo plan de trabajo
 *
 */

//Arreglo de items de actividades
let actividadesItems = [];


$('#btn-grabar-grado').click(function () {

    let $grado = $('#grado');
    let $btnGrabarGrado = $('#btn-grabar-grado');
    let $itemsAddActividad = $('.add-item-actividad');
    let $descripcion = $('#descripcion');
    let $btnPreSubmit = $('#btn-pre-submit');
    let $idGrado = $('#idGrado');


    var grado = $grado.val();
    $.ajax({
        url: '/consultas/materias?grado=' + grado,
        type: 'GET',
        dataType: 'json',
        success: function (data) {

            $($btnGrabarGrado).attr('disabled', true);
            $grado.attr('disabled', true);
            $idGrado.val(grado);


            cargarMaterias(data);

            $itemsAddActividad.attr('disabled', false);
            $descripcion.attr('disabled', false);
            $btnPreSubmit.attr('disabled', false);

            showMessageSuccess('Grado seleccionado correctamente.');

        }, error: function (xhr, status) {
            showMessageError('Error al cargar las materias ' + xhr.responseText);
        }
    });

    // cargar materias al select materia
    function cargarMaterias(data) {

        //recorrer materias con each y agregarlas al select con id materias
        let $materia = $('#materia');
        $materia.empty();
        $.each(data, function (i, item) {
            $materia.append($('<option>', {
                value: item.id, text: item.descripcion
            }));
        });
    }
});

//evento boton agregar actividad
function addItemActividad() {

    let $descripcionActividad = $('#actividad');
    let $valorActividad = $('#punteo');
    let $idMateria = $('#materia');

    //validar actividad y punteo no vacios
    if ($descripcionActividad.val() === '' || $valorActividad.val() === '') {
        showMessageWarning('Debe ingresar una descripción de actividad y su punteo.');
        return;
    }

    //validar longitud de descripcion de actividad no sea mayor a 50 caracteres
    if ($descripcionActividad.val().length > 50) {
        showMessageWarning('La descripción de actividad no debe superar los 50 caracteres.');
        return;
    }

    //validar que el punteo sea un numero
    if (isNaN($valorActividad.val())) {
        showMessageWarning('El punteo debe ser un número.');
        return;
    }

    //validar que el punteo sea mayor a 0 y menor a 100
    if ($valorActividad.val() <= 0 || $valorActividad.val() > 100) {
        showMessageWarning('El punteo debe ser mayor a 0 y menor a 100.');
        return;
    }

    // agrego actividad al arreglo de actividades
    actividadesItems.push({
        idMateria: $idMateria.val(),
        descripcionMateria: $idMateria.find('option:selected').text(),
        descripcionActividad: $descripcionActividad.val(),
        valorActividad: $valorActividad.val(),
    });

    //limpiar campos
    $descripcionActividad.val('');
    $valorActividad.val('');

    //recargar tabla con el arrenglo de actividades
    cargarTablaActividades(actividadesItems);

    //volver foco al campo materia
    $idMateria.focus();

}

// funcion para cargas los items de actividades en la tabla
function cargarTablaActividades(actividadesItems) {
    let $tablaActividades = $('#tabla-actividades');
    $tablaActividades.empty();
    $.each(actividadesItems, function (i, item) {
        $tablaActividades.append($('<tr>', {
            html:
                '<td>' + item.descripcionMateria + '</td>' +
                '<td>' + item.descripcionActividad + '</td>' +
                '<td>' + item.valorActividad + '</td> ' +
                '<td><button class="button is-danger is-small" onclick="eliminarItemActividad(' + i + ')"><i class="fas fa-trash"></i></button></td>'
        }));
    });
    // si el arreglo de actividades esta vacio, poner mensaje de ** Agregue actividades **
    if (actividadesItems.length === 0) {
        $('#tabla-actividades').html('<tr><td colspan="4"><p class="has-text-danger has-text-centered">** Agregue actividades **</p></td></tr>');
    }
}

// funcion para eliminar un item de actividad a través de su índice
function eliminarItemActividad(index) {
    actividadesItems.splice(index, 1);
    cargarTablaActividades(actividadesItems);
}

// funcion para limpiar toda la tabla de actividades
function limpiarTablaActividades() {
    actividadesItems = [];
    cargarTablaActividades(actividadesItems);
}

//validar formulario
$('#form-registrar-plan-trabajo').validate({
    errorClass: 'is-danger',
    validClass: 'is-success',
    errorElement: 'p',
    rules: {
        descripcion: {
            required: true,
            maxlength: 50,
        }
    },
    messages: {
        descripcion: {
            required: 'Debe ingresar una descripción.',
            maxlength: 'La descripción debe ser menor a 50 caracteres.'
        }
    },
    errorPlacement: function (error, element) {
        // Add the `help` class to the error element
        error.addClass("help");
        error.insertAfter(element);
    },
    submitHandler: function (form) {

        //validar que el arreglo de actividades no este vacio
        if (actividadesItems.length === 0) {
            showMessageError('Debe agregar actividades.');
            return;
        }

        //abrir modal de confirmacion
        openModal('confirmar-plan');
    }
});

//hacer submit al formulario al dar click en el boton guardar
$('#btn-pre-submit').click(function () {
    $('#form-registrar-plan-trabajo').submit();
});

//hacer submit al formulario al dar click en crear plan de trabajo modal de confirmacion
$('#btn-submit-confirmar').click(function () {

    let $form = $('#form-registrar-plan-trabajo');
    let formToJSON1 = formToJSON($form);

    //agregar actividades al JSON
    formToJSON1.actividades = actividadesItems;

    //enviar el JSON al servidor
    $.ajax({
        url: '/plan-trabajo/registrar',
        type: 'POST',
        data: JSON.stringify(formToJSON1),
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


});



