package com.pasteleria.auth.dto;

public class LoginResponse {
    private String token;
    private String message;

    // Constructor vacío
    public LoginResponse() {
    }

    // Constructor con parámetros
    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}