package com.pasteleria.usuario.service;

import com.pasteleria.usuario.model.Usuario;
import com.pasteleria.usuario.model.UsuarioDetails;
import com.pasteleria.usuario.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        System.out.println("✅ Usuario cargado: " + email);
        System.out.println("📊 ID Usuario: " + usuario.getIdUsuario());
        System.out.println("🔑 Roles: " + usuario.getRoles().stream()
                .map(rol -> "ROLE_" + rol.getNombreRol())
                .toList());

        // Retornar tu implementación personalizada de UserDetails
        return new UsuarioDetails(usuario);
    }
}