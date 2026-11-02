package com.blumbit.eblumbit.dto;

import com.blumbit.eblumbit.entities.Categorias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Acepta "detalle" (.NET) o su alias "descripcion" (Spring).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoriaDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    private String detalle;

    private String descripcion;

    public String resolveDetalle() {
        return detalle != null ? detalle : descripcion;
    }

    public Categorias toEntity() {
        Categorias categoria = new Categorias();
        categoria.setNombre(nombre);
        categoria.setDescripcion(resolveDetalle());
        return categoria;
    }
}
