/**
 * Clase para manejar la respuesta de los servicios
 */

package com.umg.controlnotas.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDataDto implements Serializable {

    private int code;
    private String message;
    private Object data;
    private List<String> errors;


}
