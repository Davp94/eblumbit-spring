package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Categorias;

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
public class CategoriaDto {

    private Integer id;

    private String nombre;

    private String detalle;

    private String descripcion;

    public static CategoriaDto fromEntityData(Categorias categoria) {
        if (categoria == null) {
            return null;
        }

        return CategoriaDto.builder()
                .id(categoria.getId() == null ? null : categoria.getId().intValue())
                .nombre(categoria.getNombre())
                .detalle(categoria.getDescripcion())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}
