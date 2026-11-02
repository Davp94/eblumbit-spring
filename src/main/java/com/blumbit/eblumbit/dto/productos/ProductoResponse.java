package com.blumbit.eblumbit.dto.productos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.blumbit.eblumbit.entities.Producto;

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
public class ProductoResponse {

    private Integer id;
    private String nombre;
    private String codigoBarra;
    private String unidadMedida;
    private String marca;
    private String imagen;
    private String descripcion;
    private BigDecimal precioVentaActual;
    private Integer stockMinimo;
    private Boolean estado;
    private String fechaRegistro;
    private Integer categoriaId;
    private String categoriaNombre;

    public static ProductoResponse fromEntity(Producto producto) {
        if (producto == null) {
            return null;
        }

        LocalDateTime fecha = producto.getFechaRegistro();
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .codigoBarra(producto.getCodigoBarra())
                .unidadMedida(producto.getUnidadMedida())
                .marca(producto.getMarca())
                .imagen(producto.getImagen())
                .descripcion(producto.getDescripcion())
                .precioVentaActual(producto.getPrecioVentaActual())
                .stockMinimo(producto.getStockMinimo())
                .estado(producto.getEstado())
                .fechaRegistro(fecha == null ? null
                        : fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")))
                .categoriaId(producto.getCategoria() == null ? null : producto.getCategoria().getId().intValue())
                .categoriaNombre(producto.getCategoria() == null ? null : producto.getCategoria().getNombre())
                .build();
    }
}
