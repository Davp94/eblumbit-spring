package com.blumbit.eblumbit.dto.productos;

import java.math.BigDecimal;

import com.blumbit.eblumbit.entities.Producto;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVentaActual;
    private String nombreCategoria;

    public static ProductoResponse fromEntity(Producto producto) {
        return ProductoResponse.builder()
        .id(producto.getId())
        .nombre(producto.getNombre())
        .descripcion(producto.getDescripcion())
        .precioVentaActual(producto.getPrecioVentaActual())
        .nombreCategoria(producto.getCategoria().getNombre())
        .build();
    }
}
