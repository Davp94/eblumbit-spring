package com.blumbit.eblumbit.dto.productos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.blumbit.eblumbit.entities.Producto;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private BigDecimal precioVentaActual;
    private String marca;
    private String codigoBarras;
    private MultipartFile imagen;
    private Integer categoriaId;

    public static Producto toEntity(ProductoRequest productoRequest) {
        return Producto.builder()
        .nombre(productoRequest.getNombre())
        .descripcion(productoRequest.getDescripcion())
        .precioVentaActual(productoRequest.getPrecioVentaActual())
        .marca(productoRequest.getMarca())
        .codigoBarra(productoRequest.getCodigoBarras())
        .build();
    }
}
