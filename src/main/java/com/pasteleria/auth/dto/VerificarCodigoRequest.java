package com.pasteleria.auth.dto;

import lombok.Data;

@Data
public class VerificarCodigoRequest {
    private String correo;
    private String codigo;
}
