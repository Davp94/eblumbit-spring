package com.blumbit.eblumbit.dto.compras;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String fecha;
    private Integer proveedorId;
    private String proveedorRazonSocial;
    private String proveedorNroIdentificacion;
    private Integer usuarioId;
    private String username;
    private BigDecimal descuentoTotal;
    private String estado;
    private String detalle;
    private String observaciones;
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private List<DetalleCompraResponse> detalleCompra;

    public static ComprasResponse fromEntity(Compra compra) {
        LocalDateTime fecha = compra.getFecha();
        return ComprasResponse.builder()
                .id(compra.getId())
                .codigo(compra.getCodigo())
                .fecha(fecha == null ? null
                        : fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")))
                .proveedorId(compra.getProveedor().getId())
                .proveedorRazonSocial(compra.getProveedor().getRazonSocial())
                .proveedorNroIdentificacion(compra.getProveedor().getNroIdentificacion())
                .usuarioId(compra.getUsuario().getId())
                .username(compra.getUsuario().getUsername())
                .descuentoTotal(compra.getDescuentoTotal())
                .estado(compra.getEstado())
                .detalle(compra.getDetalle())
                .observaciones(compra.getObservaciones())
                .build();
    }
}
