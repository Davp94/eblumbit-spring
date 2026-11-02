package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Permiso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contrato de permisos compartido por ambos backends.
 * "detalle" es el nombre canonico en .NET; "descripcion" es el alias de Spring.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermisoDto {

    private Integer id;

    private String nombre;

    private String detalle;

    private String descripcion;

    private String subject;

    private String action;

    public static PermisoDto fromEntityData(Permiso permiso) {
        if (permiso == null) {
            return null;
        }

        return PermisoDto.builder()
                .id(permiso.getId())
                .nombre(permiso.getNombre())
                .detalle(permiso.getDescripcion())
                .descripcion(permiso.getDescripcion())
                .subject(permiso.getSubject())
                .action(permiso.getAction())
                .build();
    }
}
