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
public class VentasResponse {

    private Integer id;

    private String codigo;

    private String fecha;

    private BigDecimal descuento;

    private Boolean estado;

    private String detalle;

    private Integer UsuarioId;
    
    private String usuarioNombreCompleto;

    private Integer ClienteId;

    private String clienteNombreCompleto;

    List<DetalleVentaResponse> detalleVenta;

    public static VentasResponse fromEntity(Venta venta) {
        return VentasResponse.builder()
        .id(venta.getId())
        .codigo(venta.getCodigo())
        .fecha(venta.getFecha().toString())
        .descuento(venta.getDescuentoTotal())
        .estado(venta.getEstado())
        .detalle(venta.getDetalle())
        .UsuarioId(venta.getUsuario().getId())
        .usuarioNombreCompleto(venta.getUsuario().getUsername())
        .ClienteId(venta.getCliente().getId())
        .clienteNombreCompleto(venta.getCliente().getNombreCompleto())
        .build();
    }
}
