package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Proveedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Contrato de proveedores compartido por ambos backends. */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorDto {

    private Integer id;

    private String razonSocial;

    private String nroIdentificacion;

    private String contacto;

    private String telefono;

    private String correo;

    private String observaciones;

    private Boolean estado;

    public static ProveedorDto fromEntityData(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return ProveedorDto.builder()
                .id(proveedor.getId())
                .razonSocial(proveedor.getRazonSocial())
                .nroIdentificacion(proveedor.getNroIdentificacion())
                .contacto(proveedor.getContacto())
                .telefono(proveedor.getTelefono())
                .correo(proveedor.getCorreo())
                .observaciones(proveedor.getObservaciones())
                .estado(proveedor.getEstado())
                .build();
    }
}
