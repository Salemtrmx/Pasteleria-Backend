package com.pasteleria.auth.controller;

import com.pasteleria.auth.dto.*;
import com.pasteleria.auth.service.AuthService;
import com.pasteleria.auth.service.RecuperarPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RecuperarPasswordService recuperarPasswordService;

    public AuthController(AuthService authService, RecuperarPasswordService recuperarPasswordService) {
        this.authService = authService;
        this.recuperarPasswordService = recuperarPasswordService;
    }

@PostMapping("/registrar")
public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    System.out.println("========================================");
    System.out.println("📝 REGISTRO RECIBIDO");
    System.out.println("Email: " + request.getCorreo());
    System.out.println("Nombre: " + request.getNombre());
    System.out.println("Teléfono: " + request.getTelefono());
    System.out.println("========================================");
    
    try {
        String response = authService.register(request);
        System.out.println("✅ Usuario registrado exitosamente: " + request.getCorreo());
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        System.err.println("❌ Error en registro: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("🔐 Login recibido - Email: " + request.getCorreo());
            LoginResponse response = authService.login(request);
            System.out.println("✅ Login exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            return ResponseEntity.status(403).body(new LoginResponse(null, "Credenciales inválidas"));
        }
    }

    // ============ RECUPERACIÓN DE CONTRASEÑA ============

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@RequestBody RecuperarPasswordRequest request) {
        try {
            System.out.println("📧 Solicitud de recuperación para: " + request.getCorreo());
            String mensaje = recuperarPasswordService.enviarCodigoRecuperacion(request);
            return ResponseEntity.ok(Map.of("message", mensaje));
        } catch (Exception e) {
            System.err.println("❌ Error en recuperación: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody VerificarCodigoRequest request) {
        try {
            System.out.println("🔑 Verificando código para: " + request.getCorreo());
            String mensaje = recuperarPasswordService.verificarCodigo(request);
            return ResponseEntity.ok(Map.of("message", mensaje));
        } catch (Exception e) {
            System.err.println("❌ Error verificando código: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordRequest request) {
        try {
            System.out.println("🔒 Cambiando contraseña para: " + request.getCorreo());
            String mensaje = recuperarPasswordService.cambiarPassword(request);
            return ResponseEntity.ok(Map.of("message", mensaje));
        } catch (Exception e) {
            System.err.println("❌ Error cambiando contraseña: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}