package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Proveedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CreateProveedorDto {

    @NotBlank(message = "La razon social es obligatoria")
    @Size(max = 255)
    private String razonSocial;

    @Size(max = 30)
    private String nroIdentificacion;

    @NotBlank(message = "El contacto es obligatorio")
    @Size(max = 100)
    private String contacto;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(max = 20)
    private String telefono;

    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 150)
    private String correo;

    private String observaciones;

    public Proveedor toEntity() {
        return Proveedor.builder()
                .razonSocial(razonSocial)
                .nroIdentificacion(nroIdentificacion)
                .contacto(contacto)
                .telefono(telefono)
                .correo(correo)
                .observaciones(observaciones)
                .estado(true)
                .build();
    }
}
