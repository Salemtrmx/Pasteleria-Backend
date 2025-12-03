package com.pasteleria.auth.service;

import com.pasteleria.auth.dto.LoginRequest;
import com.pasteleria.auth.dto.LoginResponse;
import com.pasteleria.auth.dto.RegisterRequest;
import com.pasteleria.auth.util.JwtUtil;
import com.pasteleria.usuario.model.RolEntity;
import com.pasteleria.usuario.model.Usuario;
import com.pasteleria.usuario.repository.RolRepository;
import com.pasteleria.usuario.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                      RolRepository rolRepository,
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager,
                      JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        System.out.println("🔐 Intento de login - Email: " + request.getCorreo());

        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getCorreo(),
                request.getContrasenia()
            )
        );

        System.out.println("✅ Autenticación exitosa");

        // Obtener UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Generar token
        String token = jwtUtil.generateToken(userDetails.getUsername());

        System.out.println("🎫 Token generado para: " + userDetails.getUsername());

        // Retornar respuesta
        return new LoginResponse(token, "Login exitoso");
    }

    public String register(RegisterRequest request) {
        System.out.println("📝 Registro de usuario: " + request.getCorreo());

        // Verificar si el usuario ya existe
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        usuario.setEstatus(1);

        // Asignar rol CLIENTE por defecto
        RolEntity rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        
        Set<RolEntity> roles = new HashSet<>();
        roles.add(rolCliente);
        usuario.setRoles(roles);

        // Guardar usuario
        usuarioRepository.save(usuario);

        System.out.println("✅ Usuario registrado exitosamente: " + usuario.getCorreo());

        return "Usuario registrado exitosamente";
    }
}