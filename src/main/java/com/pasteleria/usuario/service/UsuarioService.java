package com.pasteleria.usuario.service;

import com.pasteleria.usuario.model.RolEntity;
import com.pasteleria.usuario.model.Usuario;
import com.pasteleria.usuario.repository.RolRepository;
import com.pasteleria.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                         RolRepository rolRepository,
                         PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario createUsuario(Usuario usuario) {
        // Encriptar contraseña
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        
        // Establecer estatus activo por defecto
        usuario.setEstatus(1); // ✅ Ahora es Integer
        
        // Si no tiene roles asignados, asignar CLIENTE por defecto
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            RolEntity rolCliente = rolRepository.findByNombreRol("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
            Set<RolEntity> roles = new HashSet<>();
            roles.add(rolCliente);
            usuario.setRoles(roles);
        }
        
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Integer id, Usuario usuarioActualizado) {
        Usuario usuario = getUsuarioById(id);
        
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        
        // Si se proporciona una nueva contraseña, encriptarla
        if (usuarioActualizado.getContrasenia() != null && 
            !usuarioActualizado.getContrasenia().isEmpty()) {
            usuario.setContrasenia(passwordEncoder.encode(usuarioActualizado.getContrasenia()));
        }
        
        // Actualizar roles si se proporcionan
        if (usuarioActualizado.getRoles() != null && !usuarioActualizado.getRoles().isEmpty()) {
            usuario.setRoles(usuarioActualizado.getRoles());
        }
        
        usuario.setEstatus(usuarioActualizado.getEstatus());
        
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Integer id) {
        Usuario usuario = getUsuarioById(id);
        usuarioRepository.delete(usuario);
    }

    public void cambiarEstatus(Integer id, Integer nuevoEstatus) {
        Usuario usuario = getUsuarioById(id);
        usuario.setEstatus(nuevoEstatus); // ✅ Ahora es Integer
        usuarioRepository.save(usuario);
    }
}