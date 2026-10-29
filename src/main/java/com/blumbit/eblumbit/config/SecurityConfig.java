package com.blumbit.eblumbit.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.blumbit.eblumbit.auth.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired 
    private AuthenticationProvider authenticationProvider;

    @Autowired 
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**", "/auth/**").permitAll()
                    .requestMatchers("/api/health", "/health").permitAll()
                    .requestMatchers("/api/ventas/**", "/ventas/**").hasAnyAuthority("admin:ventas", "ROLE_VENDEDOR", "ROLE_ADMIN")
                    .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> writeError(request, response,
                            HttpStatus.UNAUTHORIZED, "AUTHENTICATION_EXCEPTION", "No autorizado"))
                    .accessDeniedHandler((request, response, accessDeniedException) -> writeError(request, response,
                            HttpStatus.FORBIDDEN, "FORBIDDEN", "Acceso denegado")))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Error al configurar el filter chain");
        }
    }

    private void writeError(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String title,
            String detail) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/problem+json");

        String traceId = request.getHeader("X-Request-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("title", title);
        body.put("status", status.value());
        body.put("detail", detail);
        body.put("instance", request.getRequestURI());
        body.put("traceId", traceId);

        MAPPER.writeValue(response.getWriter(), body);
    }
}
