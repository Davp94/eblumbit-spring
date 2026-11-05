package com.blumbit.eblumbit.dto;

import java.util.List;
import java.util.Objects;

import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.entities.Rol;

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
public class RolDto {

    private Integer id;

    private String nombre;

    private String descripcion;

    private List<Integer> permisosIds;

    public static RolDto fromEntityData(Rol rol) {
        if (rol == null) {
            return null;
        }

        List<Integer> permisosIds = rol.getPermisos() == null ? List.of() : rol.getPermisos().stream()
                .filter(Objects::nonNull)
                .map(Permiso::getId)
                .distinct()
                .toList();

        return RolDto.builder()
                .id(rol.getId())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .permisosIds(permisosIds)
                .build();
    }
}
