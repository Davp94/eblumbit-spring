package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.util.List;

public class VentasRequest {

    private Integer usuarioId;

    private Integer clienteId;

    private BigDecimal descuento;

    private String detalle;

    private List<DetalleVentaRequest> detalleVenta;
    
}
