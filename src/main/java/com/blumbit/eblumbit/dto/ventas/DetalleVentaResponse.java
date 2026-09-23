package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;

public class DetalleVentaResponse {
    private Integer id;
    private Integer ventaId;
    private Integer productoId;
    private Integer almacenId;
    private Integer cantidad;
    private String observacion;
    private BigDecimal precioUnitarioVenta;
}
