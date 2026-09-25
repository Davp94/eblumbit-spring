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
    private Integer ventaId;
    private Integer productoId;
    private Integer almacenId;
    private Integer cantidad;
    private String observacion;
    private BigDecimal precioUnitarioVenta;

    public static DetalleVentaResponse fromEntity(DetalleVenta detalleVenta) {
        return DetalleVentaResponse.builder()
        .id(detalleVenta.getId())
        .cantidad(detalleVenta.getCantidad())
        .observacion(detalleVenta.getObservaciones())
        .precioUnitarioVenta(detalleVenta.getPrecioUnitarioVenta())
        .ventaId(detalleVenta.getVenta().getId())
        .productoId(detalleVenta.getProducto().getId())
        .almacenId(detalleVenta.getAlmacen().getId())
        .build();
    }
}
