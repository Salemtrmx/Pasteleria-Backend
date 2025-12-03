package com.pasteleria.auth.service;

import com.pasteleria.auth.dto.CambiarPasswordRequest;
import com.pasteleria.auth.dto.RecuperarPasswordRequest;
import com.pasteleria.auth.dto.VerificarCodigoRequest;
import com.pasteleria.usuario.model.Usuario;
import com.pasteleria.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RecuperarPasswordService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Almacén temporal de códigos (en producción usar Redis o BD)
    private final Map<String, CodigoRecuperacion> codigosRecuperacion = new ConcurrentHashMap<>();

    // Clase interna para almacenar código y expiración
    private static class CodigoRecuperacion {
        String codigo;
        LocalDateTime expiracion;
        boolean verificado;

        CodigoRecuperacion(String codigo) {
            this.codigo = codigo;
            this.expiracion = LocalDateTime.now().plusMinutes(15); // Expira en 15 minutos
            this.verificado = false;
        }

        boolean esValido() {
            return LocalDateTime.now().isBefore(expiracion);
        }
    }

    /**
     * Paso 1: Enviar código de recuperación al correo
     */
    public String enviarCodigoRecuperacion(RecuperarPasswordRequest request) throws Exception {
        String correo = request.getCorreo().trim().toLowerCase();
        
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con este correo electrónico."));

        // Generar código de 6 dígitos
        String codigo = generarCodigo();
        
        // Guardar código en memoria
        codigosRecuperacion.put(correo, new CodigoRecuperacion(codigo));
        
        // Enviar correo
        try {
            emailService.enviarCodigoRecuperacion(correo, codigo, usuario.getNombre());
        } catch (Exception e) {
            codigosRecuperacion.remove(correo);
            System.err.println("❌ Error detallado al enviar correo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }

        System.out.println("📧 Código enviado a " + correo + ": " + codigo);
        return "Código enviado correctamente al correo " + correo;
    }

    /**
     * Paso 2: Verificar que el código sea correcto
     */
    public String verificarCodigo(VerificarCodigoRequest request) {
        String correo = request.getCorreo().trim().toLowerCase();
        String codigoIngresado = request.getCodigo().trim();

        CodigoRecuperacion recuperacion = codigosRecuperacion.get(correo);
        
        if (recuperacion == null) {
            throw new RuntimeException("No hay solicitud de recuperación para este correo.");
        }

        if (!recuperacion.esValido()) {
            codigosRecuperacion.remove(correo);
            throw new RuntimeException("El código ha expirado. Solicita uno nuevo.");
        }

        if (!recuperacion.codigo.equals(codigoIngresado)) {
            throw new RuntimeException("El código ingresado es incorrecto.");
        }

        // Marcar como verificado
        recuperacion.verificado = true;
        
        return "Código verificado correctamente.";
    }

    /**
     * Paso 3: Cambiar la contraseña
     */
    public String cambiarPassword(CambiarPasswordRequest request) {
        String correo = request.getCorreo().trim().toLowerCase();
        String codigo = request.getCodigo().trim();
        String nuevaContrasenia = request.getNuevaContrasenia();

        // Validar contraseña
        if (nuevaContrasenia == null || nuevaContrasenia.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres.");
        }

        CodigoRecuperacion recuperacion = codigosRecuperacion.get(correo);
        
        if (recuperacion == null) {
            throw new RuntimeException("No hay solicitud de recuperación para este correo.");
        }

        if (!recuperacion.esValido()) {
            codigosRecuperacion.remove(correo);
            throw new RuntimeException("El código ha expirado. Solicita uno nuevo.");
        }

        if (!recuperacion.codigo.equals(codigo)) {
            throw new RuntimeException("El código es incorrecto.");
        }

        if (!recuperacion.verificado) {
            throw new RuntimeException("Debes verificar el código primero.");
        }

        // Buscar usuario y cambiar contraseña
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        usuario.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
        usuarioRepository.save(usuario);

        // Limpiar código usado
        codigosRecuperacion.remove(correo);

        System.out.println("✅ Contraseña cambiada para: " + correo);
        return "Contraseña actualizada correctamente.";
    }

    /**
     * Genera un código numérico de 6 dígitos
     */
    private String generarCodigo() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Número entre 100000 y 999999
        return String.valueOf(codigo);
    }
}
