package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.util.List;

public class VentasResponse {

    private Integer id;

    private String codigo;

    private String fecha;

    private BigDecimal descuento;

    private Boolean estado;

    private String detalle;

    private Integer UsuariId;
    
    private String usuarioNombreCompleto;

    private Integer ClienteId;

    private String clienteNombreCompleto;

    List<DetalleVentaResponse> detalleVenta;
}
