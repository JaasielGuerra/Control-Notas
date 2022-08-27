$('#tbl-alumnos').DataTable({
    lengthMenu: [5, 10, 25, 50], language: {
        lengthMenu: "Mostrar _MENU_ alumnos",
        search: "Buscar en todos los campos: _INPUT_",
        emptyTable: "No hay registros para mostrar",
        sZeroRecords: "No se encontron resultados",
        paginate: {
            previous: "Página anterior", next: "Página siguiente"
        },
        info: "Mostrando _START_ de _END_ de _TOTAL_ alumnos"
    }
});


function verExpediente(idAlumno) {

    $.ajax({
        type: "GET",
        url: `/alumno/obtenerExpedienteAlumno?idAlumno=${idAlumno}`,
        dataType: 'json',
        success: function (data) {

            //validar si plantillaChecklist está vacio
            if ($.isEmptyObject(data['plantillaChecklists'])) {
                bulmaToast.toast({
                    message: "No hay documentos para mostrar.",
                    type: "is-warning",
                    position: 'top-center',
                    dismissible: true,
                    duration: 4000,
                    pauseOnHover: true,
                    animate: { in: 'fadeIn', out: 'fadeOut' },
                });
                return;
            }

            //recorrer data y agregar los div.colum y div.colums con inputs type radio
            agregarDetallesExpediente(data);

            //abrir modal
            openModal('modal-expediente');

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            bulmaToast.toast({
                message: "Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText,
                type: "is-danger",
                position: 'top-center',
                dismissible: true,
                duration: 4000,
                pauseOnHover: true,
                animate: { in: 'fadeIn', out: 'fadeOut' },
            });
        }
    });

    function agregarDetallesExpediente(data) {

        $('#observacion').val(data['observacion']);
        $('#expediente-si').attr('checked', data['expediente'] === 1);
        $('#expediente-no').attr('checked', data['expediente'] === 0);
        $('#idAlumno').val(data['id']);

        let $items = $('#items-documentos');
        $items.empty();//limpiar contenido

        $.each(data['plantillaChecklists'], function (i, val) {//recorrer data

            //si el indice es igual al tamaño de la lista menos 1, significa que es el ultimo elemento de la lista
            if (i === data['plantillaChecklists'].length - 1) {

                $items.append(
                    $('<div>').addClass('columns').append(
                        $('<div>').addClass('column is-6').append(
                            $('<label>').addClass('label').text(data['plantillaChecklists'][i]['descripcionDocumento']),
                            $('<div>').addClass('control').append(
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i]['idDetalleExpediente'],
                                        value: 1,
                                        checked: data['plantillaChecklists'][i]['estado'] === 1
                                    }),
                                ).append(' SI '),
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i]['idDetalleExpediente'],
                                        value: 0,
                                        checked: data['plantillaChecklists'][i]['estado'] === 0
                                    }),
                                ).append(' NO ')
                            )
                        ),
                    )
                );

                return;
            }

            if (i % 2 === 1) {// verificar si el indice es impar
                $items.append(
                    $('<div>').addClass('columns').append(
                        $('<div>').addClass('column is-6').append(
                            $('<label>').addClass('label').text(data['plantillaChecklists'][i - 1]['descripcionDocumento']),
                            $('<div>').addClass('control').append(
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i - 1]['idDetalleExpediente'],
                                        value: 1,
                                        checked: data['plantillaChecklists'][i - 1]['estado'] === 1
                                    }),
                                ).append(' SI '),
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i - 1]['idDetalleExpediente'],
                                        value: 0,
                                        checked: data['plantillaChecklists'][i - 1]['estado'] === 0
                                    }),
                                ).append(' NO ')
                            )
                        ),
                        $('<div>').addClass('column is-6').append(
                            $('<label>').addClass('label').text(data['plantillaChecklists'][i]['descripcionDocumento']),
                            $('<div>').addClass('control').append(
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i]['idDetalleExpediente'],
                                        value: 1,
                                        checked: data['plantillaChecklists'][i]['estado'] === 1
                                    }),
                                ).append(' SI '),
                                $('<label>').addClass('radio').append(
                                    $('<input>').attr({
                                        type: 'radio',
                                        name: data['plantillaChecklists'][i]['idDetalleExpediente'],
                                        value: 0,
                                        checked: data['plantillaChecklists'][i]['estado'] === 0
                                    }),
                                ).append(' NO ')
                            )
                        ),
                    )
                );
            }

        });
    }

}

// guardar datos checklist expediente
$('#modal-checklist-expediente').submit(function (e) {
    e.preventDefault();

    let formDatosExpediente = {}; //array de objetos con los datos del expediente alumno
    let formJSON = formToJSON(this); //convertir formulario a json

    //agregar expediente y observacion a formDatosExpediente
    formDatosExpediente['expediente'] = formJSON['expediente'];
    formDatosExpediente['observacion'] = formJSON['observacion'];
    formDatosExpediente['id'] = formJSON['id'];

    // eliminar expediente y observacion del array formJSON
    delete formJSON['expediente'];
    delete formJSON['observacion'];
    delete formJSON['id'];

    formDatosExpediente['plantillaChecklists'] = llenarPlantillaChecklist(formJSON);


    //poner efecto carga boton submit
    loadingBtn("#btn-guardar-checklist");

    $.ajax({
        type: "POST",
        url: "/alumno/guardarChecklistExpediente",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(formDatosExpediente),
        success: function (response) {

            removeLoadingBtn("#btn-guardar-checklist");
            alert("¡Datos guardados con éxito!");
            window.location = "/alumno/consultar";

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            bulmaToast.toast({
                message: "Error " + XMLHttpRequest.status + ", respuesta del servidor: " + XMLHttpRequest.responseText,
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

// funcion para llenar plantillaChecklists
function llenarPlantillaChecklist(formJSON) {
    let plantilla = [];//limpiar array
    Object.keys(formJSON).forEach(function (key) {
        plantilla.push({idDetalleExpediente: key, estado: formJSON[key]});
    });
    return plantilla;
}

function cambiarGradoAlumno(idAlumno) {

    $.ajax({
        type: "GET",
        url: `/alumno/obtenerAsignacionAlumno?idAlumno=${idAlumno}`,
        dataType: 'json',
        success: function (data) {

            let idSeccion = data["idSeccionAlumno"];
            let $grados = $("#gradoSeccionReasignar");
            $grados.find("option").slice(1).remove();//slice salta el primero item

            for (const i of data["gradoSeccionList"]) {
                //agregar nuevos items
                $grados.append(new Option(`${i['descripcionGrado']} - ${i['descripcionSeccion']}`, i['idSeccion'], false, i['idSeccion'] === idSeccion));
            }

            $('#idAlumnoReasignar').val(idAlumno);
            openModal('modal-grado')

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            bulmaToast.toast({
                message: "Error .", type: "is-danger",
                position: 'top-center',
                dismissible: true,
                duration: 4000,
                pauseOnHover: true,
                animate: { in: 'fadeIn', out: 'fadeOut' },
            });
        }
    });

}

function reasignarAlumno() {

    let asignacion = {
        idSeccionAlumno: $('#gradoSeccionReasignar').val(), idAlumno: $('#idAlumnoReasignar').val()
    }

    //poner efecto carga boton submit
    loadingBtn("#btn-guardar-reasignacion");

    $.ajax({
        type: "POST",
        url: "/alumno/cambiarAsignacion",
        contentType: "application/json",
        dataType: 'json',
        data: JSON.stringify(asignacion),
        success: function (response) {

            removeLoadingBtn("#btn-guardar-reasignacion");
            alert("¡Alumno reasignado con éxito!");
            window.location = "/alumno/consultar";
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

            removeLoadingBtn("#btn-guardar-reasignacion");
        }
    });
}

function preEliminarAlumno(idAlumno) {

    $('#id-alumno-eliminar').val(idAlumno);
    openModal('modal-confirmacion-eliminar');
}