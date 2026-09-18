package com.blumbit.eblumbit.dto;

import java.util.List;

import com.blumbit.eblumbit.entities.Usuario;

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

    private String username;

    private String correo;

    private String password;

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
