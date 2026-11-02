package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Permiso;

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
public class CreatePermisoDto {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    private String detalle;

    @Size(max = 255)
    private String descripcion;

    @NotBlank(message = "El subject es obligatorio")
    @Size(max = 100)
    private String subject;

    @NotBlank(message = "La action es obligatoria")
    @Size(max = 100)
    private String action;

    public String resolveDetalle() {
        return detalle != null ? detalle : descripcion;
    }

    public Permiso toEntity() {
        Permiso permiso = new Permiso();
        permiso.setNombre(nombre);
        permiso.setDescripcion(resolveDetalle());
        permiso.setSubject(subject);
        permiso.setAction(action);
        return permiso;
    }
}
