package com.blumbit.eblumbit.dto.compras;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.blumbit.eblumbit.entities.Compra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComprasResponse {

    private Integer id;
    private String codigo;
    private LocalDateTime fecha;
    private Integer proveedorId;
    private String proveedorRazonSocial;
    private Integer usuarioId;
    private String usuarioNombre;
    private BigDecimal descuentoTotal;
    private String estado;
    private String detalle;
    private String observaciones;
    private List<DetalleCompraResponse> detalleCompra;

    public static ComprasResponse fromEntity(Compra compra) {
        return ComprasResponse.builder()
            .id(compra.getId())
            .codigo(compra.getCodigo())
            .fecha(compra.getFecha())
            .proveedorId(compra.getProveedor().getId())
            .proveedorRazonSocial(compra.getProveedor().getRazonSocial())
            .usuarioId(compra.getUsuario().getId())
            .usuarioNombre(compra.getUsuario().getUsername())
            .descuentoTotal(compra.getDescuentoTotal())
            .estado(compra.getEstado())
            .detalle(compra.getDetalle())
            .observaciones(compra.getObservaciones())
            .build();
    }
}