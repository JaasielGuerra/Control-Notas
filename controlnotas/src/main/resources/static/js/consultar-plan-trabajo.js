/**
 * Funcionalidad para la consulta de plan de trabajo
 */
(function () {

    let actividadesItems = [];//arreglo para guardar los items de actividades en memoria
    let actividadesItemsEliminados = [];//arreglo para guardar los items de actividades eliminados en memoria
    let tablaActividades = $('#tabla-actividades');


    /**
     * Evento para ejecutar funcion consultarActividades al hacer click en el boton actividades
     */
    $('#list-planes-trabajo').on('click', '.btn-actividades', function () {
        let idPlanTrabajo = $(this).data('id');
        let idGrado = $(this).data('id-grado');
        verActividadesPlanTrabajo(idPlanTrabajo, idGrado);
    });

    /**
     * Evento para eliminar un item de actividad a través de su índice
     */
    tablaActividades.on('click', '.btn-eliminar-actividad', function () {
        let index = $(this).data('index');
        eliminarItemActividad(index);
    });

    /**
     * evento para cambiar el valor de la actividad en el arreglo de actividades al cambiar el valor del input
     */
    tablaActividades.on('change', 'input', function () {
        let index = $(this).closest('tr').index();
        actividadesItems[index].valorActividad = $(this).val();
    });

    /**
     * Evento btn-agregar-actividad para agregar una actividad a la tabla
     */
    $('#btn-agregar-actividad').click(function () {
        addItemActividad();
    });

    /**
     * Evento para ejecutar funcion addItemActividad al presionar enter en el input de punteo
     */
    $('#punteo').keydown(function (e) {
        if (e.which === 13) {
            addItemActividad();
        }
    });

    /**
     * Evento btn-guardar-plan para guardar el plan de trabajo
     */
    $('#btn-guardar-plan').click(function () {
        guardarCambiosPlanTrabajo();
    });

    /**
     * Consultar las actividades de un plan de trabajo y cargarlas en la tabla del modal de actividades
     * @param idPlanTrabajo
     * @param idGrado
     */
    function verActividadesPlanTrabajo(idPlanTrabajo, idGrado) {

        //poner id en btn-guardar-plan para saber a que plan de trabajo se le van a guardar los cambios
        $('#btn-guardar-plan').attr('data-id-plan', idPlanTrabajo);

        $.ajax({
            url: "/plan-trabajo/consultar-actividades?idPlanTrabajo=" + idPlanTrabajo,
            type: "GET",
            dataType: "json",
            success: function (data) {

                //agregar items a actividadesItems
                agregarItemsArregloActividades(data);

                //cargar la tabla con los items de actividades
                cargarTablaActividades();

                consultarMateriasGrado(idGrado);

                //abrir modal
                openModal('modal-actividades');

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                showMessageError("Error interno en el servidor. " + XMLHttpRequest.responseText);
            }
        });
    }

    /**
     * Consultar materias de un grado
     * @param idGrado
     */
    function consultarMateriasGrado(idGrado) {

        $.ajax({
            url: '/consultas/materias?grado=' + idGrado,
            type: 'GET',
            dataType: 'json',
            success: function (data) {

                cargarMaterias(data);

            }, error: function (xhr, status) {
                showMessageError('Error al cargar las materias ' + xhr.responseText);
            }
        });

        /** cargar materias al select materia
         * @param data
         */
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

    }


    /**
     * Funcion para agregar los items de actividades al arreglo de actividades
     * @param data
     */
    function agregarItemsArregloActividades(data) {
        actividadesItems = [];// vaciar el arreglo de actividades
        actividadesItemsEliminados = [];

        //agregar items al arreglo actividadesItems
        $.each(data, function (i, item) {
            actividadesItems.push({
                idMateria: item.idMateriaId,
                descripcionMateria: item.idMateriaDescripcion,
                descripcionActividad: item.descripcionActividad,
                valorActividad: item.valorActividad,
                estado: item.estado,
                id: item.id
            });
        });
    }


    /**
     * Cargar la tabla de actividades con los items de actividades
     */
    function cargarTablaActividades() {
        tablaActividades.empty();
        $.each(actividadesItems, function (i, item) {

            //si el estado es 1 (activo) se agrega el item a la tabla
            if (item.estado === 1) {
                tablaActividades.append($('<tr>', {
                    html:
                        '<td class="is-size-7">' + item.descripcionMateria + '</td>' +
                        '<td class="is-size-7">' + item.descripcionActividad + '</td>' +
                        '<td>' + '<input onfocus="this.select()" class="input is-small is-rounded" style="width: 5rem" type="number" placeholder="Valor" value="' + item.valorActividad + '">' + '</td> ' +
                        '<td><button class="button is-danger is-small btn-eliminar-actividad" data-index="' + i + '"><i class="fas fa-trash"></i></button></td>'
                }));
            }
        });

        // si todos los items del arreglo tienen propiedad estado en 0, poner mensaje de ** Agregue actividades **
        if (actividadesItems.every(item => item.estado === 0)) {
            tablaActividades.html('<tr><td colspan="4"><p class="has-text-danger has-text-centered">** Agregue actividades **</p></td></tr>');
        }
    }


    /**
     * Funcion para agregar actividad
     */
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
            estado: 1
        });

        //limpiar campos
        $descripcionActividad.val('');
        $valorActividad.val('');

        //recargar tabla con el arrenglo de actividades
        cargarTablaActividades();

        //volver foco al campo materia
        $idMateria.focus();

        showMessageSuccess('Actividad agregada correctamente.');

    }

    /**
     * Eliminar un item de actividad de la tabla de actividades
     * @param index
     */
    function eliminarItemActividad(index) {

        actividadesItems[index].estado = 0;
        actividadesItemsEliminados.push(actividadesItems[index]);
        actividadesItems.splice(index, 1);

        console.log(actividadesItemsEliminados);

        cargarTablaActividades();
        showMessageSuccess('Actividad removida correctamente.');
    }

    /**
     * Funcion para guardar los cambios realizados en las actividades del plan trabajo
     */
    function guardarCambiosPlanTrabajo() {

        $.each(actividadesItemsEliminados, function (i, item) {
            actividadesItems.push(item);
        });

        //obtener id atributo data-id-plan del boton guardar
        let idPlanTrabajo = $('#btn-guardar-plan').data('id-plan');

        $.ajax({
            url: '/plan-trabajo/' + idPlanTrabajo,
            type: 'PUT',
            data: JSON.stringify(actividadesItems),
            contentType: 'application/json',
            dataType: 'json',
            success: function (data) {

                //si el code es 1
                if (data.code === 1) {
                    closeModal(document.getElementById('modal-actividades'));
                    showMessageSuccess(data.message);
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




