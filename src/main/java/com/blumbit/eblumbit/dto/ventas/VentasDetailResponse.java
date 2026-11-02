package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VentasDetailResponse extends VentasResponse {

    private List<DetalleVentaResponse> detalleVenta;

    public VentasDetailResponse(Integer id, String codigo, String fecha, BigDecimal descuentoTotal,
            String observacion, Integer usuarioId, String username, Integer clienteId,
            String nroIdentificacionCliente, List<DetalleVentaResponse> detalleVenta) {
        super(id, codigo, fecha, descuentoTotal, observacion, usuarioId, username, clienteId,
                nroIdentificacionCliente);
        this.detalleVenta = detalleVenta;
    }

    public static VentasDetailResponse fromEntity(com.blumbit.eblumbit.entities.Venta venta,
            List<DetalleVentaResponse> detalleVenta) {
        VentasResponse base = VentasResponse.fromEntity(venta);
        return new VentasDetailResponse(base.getId(), base.getCodigo(), base.getFecha(),
                base.getDescuentoTotal(), base.getObservacion(), base.getUsuarioId(), base.getUsername(),
                base.getClienteId(), base.getNroIdentificacionCliente(), detalleVenta);
    }
}
