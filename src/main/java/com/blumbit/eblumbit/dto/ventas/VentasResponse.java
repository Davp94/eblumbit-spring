package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter FECHA_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private Integer id;
    private String codigo;
    private String fecha;
    private BigDecimal descuentoTotal;
    private String observacion;
    private Integer usuarioId;
    private String username;
    private Integer clienteId;
    private String nroIdentificacionCliente;

    public static VentasResponse fromEntity(Venta venta) {
        LocalDateTime fecha = venta.getFecha();
        return VentasResponse.builder()
                .id(venta.getId())
                .codigo(venta.getCodigo())
                .fecha(fecha == null ? null : fecha.format(FECHA_FORMAT))
                .descuentoTotal(venta.getDescuentoTotal())
                .observacion(venta.getDetalle())
                .usuarioId(venta.getUsuario() == null ? null : venta.getUsuario().getId())
                .username(venta.getUsuario() == null ? null : venta.getUsuario().getUsername())
                .clienteId(venta.getCliente() == null ? null : venta.getCliente().getId())
                .nroIdentificacionCliente(venta.getCliente() == null ? null
                        : venta.getCliente().getNroIdentificacion())
                .build();
    }
}
