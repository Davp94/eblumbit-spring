package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Almacen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Contrato de almacenes compartido por ambos backends. */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlmacenDto {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer sucursalId;
    private String sucursalNombre;

    public static AlmacenDto fromEntityData(Almacen almacen) {
        if (almacen == null) {
            return null;
        }
        return AlmacenDto.builder()
                .id(almacen.getId())
                .codigo(almacen.getCodigo())
                .nombre(almacen.getNombre())
                .descripcion(almacen.getDescripcion())
                .sucursalId(almacen.getSucursal() == null ? null : almacen.getSucursal().getId().intValue())
                .sucursalNombre(almacen.getSucursal() == null ? null : almacen.getSucursal().getNombre())
                .build();
    }
}
