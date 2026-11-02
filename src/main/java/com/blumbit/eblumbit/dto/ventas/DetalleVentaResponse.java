package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;

import com.blumbit.eblumbit.entities.DetalleVenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaResponse {

    private Integer id;
    private Integer cantidad;
    private BigDecimal precioUnitarioVenta;
    private String observaciones;
    private Integer productoId;
    private String productoNombre;
    private Integer ventaId;
    private Integer almacenId;
    private String almacenNombre;

    public static DetalleVentaResponse fromEntity(DetalleVenta detalleVenta) {
        return DetalleVentaResponse.builder()
                .id(detalleVenta.getId())
                .cantidad(detalleVenta.getCantidad())
                .precioUnitarioVenta(detalleVenta.getPrecioUnitarioVenta())
                .observaciones(detalleVenta.getObservaciones())
                .productoId(detalleVenta.getProducto().getId())
                .productoNombre(detalleVenta.getProducto().getNombre())
                .ventaId(detalleVenta.getVenta().getId())
                .almacenId(detalleVenta.getAlmacen().getId())
                .almacenNombre(detalleVenta.getAlmacen().getNombre())
                .build();
    }
}
