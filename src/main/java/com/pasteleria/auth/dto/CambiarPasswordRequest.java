package com.pasteleria.auth.dto;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String correo;
    private String codigo;
    private String nuevaContrasenia;
}
