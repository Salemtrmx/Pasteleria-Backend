/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pasteleria.auth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pasteleria.auth.Filter.UserAuthenticationFilter;

@Configuration
public class SecurityConfig {
    // Filtro de autenticación JWT
    private final UserAuthenticationFilter userAuthenticationFilter;
    // Inyección de dependencia del filtro de autenticación JWT
    public SecurityConfig(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    // encriptador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Configuración de seguridad HTTP 
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                    // ✅ PERMITIR OPTIONS (preflight CORS)
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    // RUTAS PÚBLICAS DE AUTENTICACIÓN
                    .requestMatchers("/api/auth/**").permitAll()

                    // RUTAS PÚBLICAS DEL USUARIO (registro o recuperar contraseña)
                    .requestMatchers("/api/public/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/inventario/**").permitAll()

                    // RUTAS PÚBLICAS PARA SABOR, TAMAÑO Y DECORACIÓN (GET)
                    .requestMatchers(HttpMethod.GET, "/api/sabor/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/tamanio/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/decoracion/**").permitAll()

                    // RUTAS PRIVADAS PARA ADMIN
                    .requestMatchers("/api/users/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.POST, "/api/inventario/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.PUT, "/api/inventario/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.DELETE, "/api/inventario/**").hasRole("ADMINISTRADOR")
                    
                    // CRUD de sabor, tamaño y decoración solo para ADMIN
                    .requestMatchers(HttpMethod.POST, "/api/sabor/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.PUT, "/api/sabor/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.DELETE, "/api/sabor/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.POST, "/api/tamanio/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.PUT, "/api/tamanio/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.DELETE, "/api/tamanio/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.POST, "/api/decoracion/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.PUT, "/api/decoracion/**").hasRole("ADMINISTRADOR")
                    .requestMatchers(HttpMethod.DELETE, "/api/decoracion/**").hasRole("ADMINISTRADOR")

                    // RUTAS DE PAGOS (MERCADO PAGO)
                    .requestMatchers("/api/pagos/webhook").permitAll()
                    .requestMatchers("/api/pagos/**").authenticated()

                    // RUTAS DE PEDIDOS
                    .requestMatchers("/api/pedidos/**").authenticated()
                    
                    // RUTAS DE DETALLES DE PEDIDOS
                    .requestMatchers("/api/detalle-pedidos/**").authenticated()
                    
                    .requestMatchers("/api/estadisticas/**").hasRole("ADMINISTRADOR")

                    // DOCUMENTACIÓN SWAGGER LIBRE
                    .requestMatchers(
                            "/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/webjars/**")
                    .permitAll()

                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Permitir orígenes específicos
    configuration.setAllowedOriginPatterns(List.of("*")); // ✅ Más permisivo para desarrollo
    
    // Permitir métodos HTTP específicos
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    
    // Permitir credenciales
    configuration.setAllowCredentials(true);
    
    // Permitir TODOS los encabezados
    configuration.setAllowedHeaders(List.of("*")); // ✅ Cambio importante
    
    // Exponer headers en la respuesta
    configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
    
    // Tiempo de cache para preflight
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}