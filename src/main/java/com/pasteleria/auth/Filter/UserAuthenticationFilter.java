package com.pasteleria.auth.Filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pasteleria.auth.util.JwtUtil;
import com.pasteleria.usuario.service.UsuarioDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioDetailsService usuarioDetailsService;

    // Lista de rutas públicas que NO requieren JWT
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/api/auth/",
        "/api/public/",
        "/api-docs/",
        "/swagger-ui/",
        "/v3/api-docs/",
        "/swagger-resources/",
        "/webjars/"
    );

    public UserAuthenticationFilter(JwtUtil jwtUtil, UsuarioDetailsService usuarioDetailsService) {
        this.jwtUtil = jwtUtil;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("🔍 Filtro JWT - Path: " + path + " | Method: " + method);

        // ✅ SI ES RUTA PÚBLICA, CONTINUAR SIN VALIDAR JWT
        if (isPublicPath(path)) {
            System.out.println("✅ Ruta pública detectada, omitiendo validación JWT");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ PERMITIR GET en /api/inventario/** sin JWT
        if (path.startsWith("/api/inventario") && method.equals("GET")) {
            System.out.println("✅ GET en inventario, omitiendo validación JWT");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ PERMITIR GET en /api/sabor, /api/tamanio, /api/decoracion sin JWT
        if ((path.startsWith("/api/sabor") || path.startsWith("/api/tamanio") || path.startsWith("/api/decoracion")) && method.equals("GET")) {
            System.out.println("✅ GET en catálogo de atributos, omitiendo validación JWT");
            filterChain.doFilter(request, response);
            return;
        }

        // 🔒 PARA RUTAS PROTEGIDAS, VALIDAR JWT
        String authHeaderDebug = request.getHeader("Authorization");
        System.out.println("🔐 Header Authorization: " + authHeaderDebug);

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remover "Bearer " prefijo
            try {
                email = jwtUtil.extractEmail(token);
                System.out.println("📧 Email extraído del token: " + email);
            } catch (Exception e) {
                System.out.println("❌ Token inválido: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ No se encontró token JWT en la petición");
        }

        // Validar token y cargar usuario en SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                System.out.println("✅ Token válido para usuario: " + email);
                System.out.println("🔑 Authorities del usuario: " + userDetails.getAuthorities());
                
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Usuario autenticado en SecurityContext con authorities: " + authToken.getAuthorities());
            } else {
                System.out.println("❌ Token inválido o expirado");
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si el path es una ruta pública que no requiere autenticación
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}