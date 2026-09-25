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
public class DetalleVentaRequest {

    private Integer cantidad;

    private BigDecimal precioUnitarioVenta;

    private String observacion;

    private Integer productoId;

    private Integer almacenId;

    public static DetalleVenta toEntity(DetalleVentaRequest detalleVentaRequest) {
        return DetalleVenta.builder()
        .cantidad(detalleVentaRequest.getCantidad())
        .precioUnitarioVenta(detalleVentaRequest.getPrecioUnitarioVenta())
        .observaciones(detalleVentaRequest.getObservacion())
        .build();
    }
}
