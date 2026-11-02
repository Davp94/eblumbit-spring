package com.blumbit.eblumbit.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.blumbit.eblumbit.entities.Cliente;

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
public class ClienteDto {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Integer id;

    private String nombreCompleto;

    private String nroIdentificacion;

    private String fechaNacimiento;

    private String telefono;

    private String correo;

    private Boolean estado;

    public static ClienteDto fromEntityData(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        LocalDate fecha = cliente.getFechaNacimiento();
        return ClienteDto.builder()
                .id(cliente.getId())
                .nombreCompleto(cliente.getNombreCompleto())
                .nroIdentificacion(cliente.getNroIdentificacion())
                .fechaNacimiento(fecha == null ? null : fecha.format(DATE_FORMAT))
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .estado(cliente.getEstado())
                .build();
    }
}
