/**
 * Clase para manejar la respuesta de los servicios
 */

package com.umg.controlnotas.model.custom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseData {

    private int code;
    private String message;
    private Object data;
    private List<String> errors;


}
