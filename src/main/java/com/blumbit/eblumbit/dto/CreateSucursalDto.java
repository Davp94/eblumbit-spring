package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Sucursal;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class CreateSucursalDto {

    private String nombre;

    private String direccion;

    private String telefono;

    private String ciudad;

    public static Sucursal toEntity(CreateSucursalDto createSucursalDto) {
        if (createSucursalDto == null) {
            return null;
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(createSucursalDto.getNombre());
        sucursal.setDireccion(createSucursalDto.getDireccion());
        sucursal.setTelefono(createSucursalDto.getTelefono());
        sucursal.setCiudad(createSucursalDto.getCiudad());
        return sucursal;
    }

}
