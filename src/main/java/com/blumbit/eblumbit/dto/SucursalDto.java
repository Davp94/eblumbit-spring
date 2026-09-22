package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Sucursal;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class SucursalDto {

    private Long id;

    private String nombre;

    private String direccion;

    private String telefono;

    private String ciudad;

    public static SucursalDto fromEntityData(Sucursal sucursal) {
        if (sucursal == null) {
            return null;
        }

        return SucursalDto.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .telefono(sucursal.getTelefono())
                .ciudad(sucursal.getCiudad())
                .build();
    }
}
