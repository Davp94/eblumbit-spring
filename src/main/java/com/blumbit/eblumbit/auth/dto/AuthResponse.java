package com.blumbit.eblumbit.auth.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contrato de respuesta de autenticacion, identico al backend .NET
 * (EBlumbit.Dto.Auth.AuthResponse).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private Integer usuarioId;

    private Integer identifier;

    private Integer expirationMinutes;

    private Long expiration;

    private String email;

    private List<String> roles;

    private List<String> permissions;
}
