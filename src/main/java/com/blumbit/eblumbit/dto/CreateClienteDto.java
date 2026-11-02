package com.blumbit.eblumbit.dto;

import java.time.LocalDate;

import com.blumbit.eblumbit.entities.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CreateClienteDto {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 200)
    private String nombreCompleto;

    @NotBlank(message = "El numero de identificacion es obligatorio")
    @Size(max = 30)
    private String nroIdentificacion;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "La fecha de nacimiento debe tener formato yyyy-MM-dd")
    private String fechaNacimiento;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(max = 100)
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 150)
    private String correo;

    public LocalDate resolveFechaNacimiento() {
        return LocalDate.parse(fechaNacimiento);
    }

    public Cliente toEntity() {
        return Cliente.builder()
                .nombreCompleto(nombreCompleto)
                .nroIdentificacion(nroIdentificacion)
                .fechaNacimiento(resolveFechaNacimiento())
                .telefono(telefono)
                .correo(correo)
                .estado(true)
                .build();
    }
}
