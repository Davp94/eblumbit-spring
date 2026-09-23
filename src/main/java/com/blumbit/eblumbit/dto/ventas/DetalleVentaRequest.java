package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;

public class DetalleVentaRequest {

    private Integer cantidad;

    private BigDecimal precioUnitarioVenta;

    private String observacion;

    private Integer productoId;

    private Integer almacenId;
}
