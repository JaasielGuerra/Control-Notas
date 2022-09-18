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

    public static final int SUCCESS = 1;
    public static final int ERROR = 0;

    private int code;
    private String message;
    private Object data;
    private List<String> errors;


}
