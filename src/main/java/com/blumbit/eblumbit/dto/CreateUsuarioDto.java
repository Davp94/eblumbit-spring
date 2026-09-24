package com.blumbit.eblumbit.dto;

import java.util.List;

import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.validation.annotation.UniqueName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class CreateUsuarioDto {

    @NotNull(message = "El nombre de usuario es requerido")
    @NotBlank(message = "El nombre de usuario no puede ser vacio")
    @UniqueName(fieldName = "username") 
    @Size(max=50, message = "El nombre de usuario debe tener maximo 50 caracteres") 
    private String username;

    @NotBlank 
    @Email(message = "El correo debe tener un formato válido") 
    @Size(max=200) 
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@blumbit\\.com$",
        message = "El correo debe pertenecer al dominio blumbit.com"
    )
    private String correo; // <nombre>@<domain>.xxx

    @NotNull 
    @NotBlank 
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,16}$",
        message = "La contraseña debe tener entre 8 y 16 caracteres, incluir al menos 1 numero, una mayuscula y un caracter especial"
    ) 
    private String password;

    @NotEmpty(message = "Se debe tener al menos un rol asignado para el usuario")
    private List<Integer> rolIds;


    public static Usuario toEntity(CreateUsuarioDto createUsuarioDto) {
        return Usuario.builder()
        .email(createUsuarioDto.getCorreo())
        .password(createUsuarioDto.getPassword())
        .username(createUsuarioDto.getUsername())
        .estado(true)
        .build();
    }
}
