package com.pasteleria.auth.dto;

public class RegisterRequest {
    private String nombre;
    private String telefono;
    private String correo;
    private String contrasenia;

    public RegisterRequest() {}

    public RegisterRequest(String nombre, String telefono, String correo, String contrasenia) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

}
