package com.blumbit.eblumbit.dto;

import java.util.List;
import java.util.Objects;

import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.entities.RolUsuario;
import com.blumbit.eblumbit.entities.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contrato de usuario compartido por ambos backends.
 * Incluye alias (correo) y campos que consumen los frontends Angular y React.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Integer id;

    private String username;

    private String nombres;

    private String apellidos;

    private String email;

    private String correo;

    private Boolean state;

    private List<Integer> rolIds;

    private List<Integer> sucursalId;

    private List<Integer> permissionsIds;

    public static UsuarioDto fromEntityData(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        List<RolUsuario> roles = usuario.getRolesUsuario() == null ? List.of() : usuario.getRolesUsuario();

        List<Integer> rolIds = roles.stream()
                .map(RolUsuario::getRol)
                .filter(Objects::nonNull)
                .map(rol -> rol.getId())
                .distinct()
                .toList();

        List<Integer> permissionsIds = roles.stream()
                .map(RolUsuario::getRol)
                .filter(Objects::nonNull)
                .filter(rol -> rol.getPermisos() != null)
                .flatMap(rol -> rol.getPermisos().stream())
                .filter(Objects::nonNull)
                .map(Permiso::getId)
                .distinct()
                .toList();

        return UsuarioDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .correo(usuario.getEmail())
                .state(usuario.isEstado())
                .rolIds(rolIds)
                .sucursalId(List.of())
                .permissionsIds(permissionsIds)
                .build();
    }
}
