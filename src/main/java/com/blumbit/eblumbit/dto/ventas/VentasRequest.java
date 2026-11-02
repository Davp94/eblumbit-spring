package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentasRequest {

    private BigDecimal descuentoTotal;

    private BigDecimal descuento;

    private String observacion;

    private String detalle;

    private Integer clienteId;

    private Integer usuarioId;

    private List<DetalleVentaRequest> detalleVenta;

    public BigDecimal resolveDescuento() {
        return descuentoTotal != null ? descuentoTotal : descuento;
    }

    public String resolveObservacion() {
        return observacion != null ? observacion : detalle;
    }
}
