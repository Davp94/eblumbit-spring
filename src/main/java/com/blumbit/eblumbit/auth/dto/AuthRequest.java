package com.blumbit.eblumbit.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {

    /**
     * Email del usuario. Es el identificador canonico (igual que el backend .NET).
     */
    private String email;

    /**
     * Campo legado: se mantiene para clientes antiguos que enviaban username.
     * Solo se usa si {@link #email} viene vacio.
     */
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}
