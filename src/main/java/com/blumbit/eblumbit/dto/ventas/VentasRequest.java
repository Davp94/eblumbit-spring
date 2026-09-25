package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.util.List;

import com.blumbit.eblumbit.entities.Venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class VentasRequest {

    private Integer usuarioId;

    private Integer clienteId;

    private BigDecimal descuento;

    private String detalle;

    private List<DetalleVentaRequest> detalleVenta;
    
    public static Venta toEntity(VentasRequest ventaRequest) {
        return Venta.builder()
        .descuentoTotal(ventaRequest.getDescuento())
        .detalle(ventaRequest.getDetalle())
        .build();
    }
}
