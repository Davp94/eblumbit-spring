package com.blumbit.eblumbit.dto;

import java.util.List;

import com.blumbit.eblumbit.entities.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Superset compatible con ambos frontends: acepta "email" o su alias "correo",
 * y el password es opcional (los formularios actuales no lo envian).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioDto {

    private Integer id;

    @NotNull(message = "El nombre de usuario es requerido")
    @NotBlank(message = "El nombre de usuario no puede ser vacio")
    @Size(max = 50, message = "El nombre de usuario debe tener maximo 50 caracteres")
    private String username;

    @Size(max = 200)
    private String email;

    @Size(max = 200)
    private String correo;

    private String password;

    private String nombres;

    private String apellidos;

    private List<Integer> rolIds;

    public String resolveEmail() {
        String value = (email != null && !email.isBlank()) ? email : correo;
        return value == null ? null : value.trim();
    }

    public static Usuario toEntity(CreateUsuarioDto dto) {
        return Usuario.builder()
                .email(dto.resolveEmail())
                .username(dto.getUsername())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .estado(true)
                .build();
    }
}
