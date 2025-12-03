package com.pasteleria.auth.util;

import com.pasteleria.usuario.model.Usuario;
import com.pasteleria.usuario.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Key SECRET_KEY;
    private final long EXPIRATION_TIME;
    private final UsuarioRepository usuarioRepository;

    public JwtUtil(
            UsuarioRepository usuarioRepository,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.usuarioRepository = usuarioRepository;
        this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.EXPIRATION_TIME = expiration;
    }

public String generateToken(String email) {
    Usuario usuario = usuarioRepository.findByCorreo(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Map<String, Object> claims = new HashMap<>();
    claims.put("idUsuario", usuario.getIdUsuario());
    claims.put("nombre", usuario.getNombre());
    
    // ✅ Obtener el primer rol del usuario
    if (!usuario.getRoles().isEmpty()) {
        String rol = usuario.getRoles().iterator().next().getNombreRol();
        claims.put("rol", rol); // ← IMPORTANTE: Asegúrate de que esto exista
    }

    return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SECRET_KEY)
            .compact();
}

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}