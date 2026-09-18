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
public class UsuarioDto {

    private Integer id;

    private String username;

    private String correo;

    private List<Integer> rolIds;

    private List<Integer> sucursalId;

    public static UsuarioDto fromEntityData(Usuario usuario){
        return UsuarioDto.builder()
        .id(usuario.getId())
        .username(usuario.getUsername())
        .correo(usuario.getEmail())
        .rolIds(List.of())
        .sucursalId(List.of())
        .build();
    }
}
