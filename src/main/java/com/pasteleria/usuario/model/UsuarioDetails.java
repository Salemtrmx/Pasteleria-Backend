package com.pasteleria.usuario.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementación personalizada de UserDetails para Spring Security
 * Envuelve la entidad Usuario y proporciona información de autenticación
 */
public class UsuarioDetails implements UserDetails {
    
    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método para obtener el Usuario original
     * Útil para acceder a propiedades adicionales del usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Obtiene los roles del usuario y los convierte a authorities de Spring Security
     * Ahora usa la relación ManyToMany con la tabla USUARIO_ROL
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getPassword() {
        return usuario.getContrasenia();
    }
    
    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * Verifica si la cuenta está habilitada
     * estatus: 1 = activo, 0 = inactivo
     */
    @Override
    public boolean isEnabled() {
        return usuario.getEstatus() == 1;
    }
}