package com.blumbit.eblumbit.dto.compras;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComprasRequest {

    @NotBlank
    private String codigo;

    private LocalDateTime fecha;

    @NotNull
    private Integer proveedorId;

    @NotNull
    private Integer usuarioId;

    private BigDecimal descuentoTotal;

    @NotBlank
    private String estado;

    private String detalle;

    private String observaciones;

    @NotEmpty
    private List<@Valid DetalleCompraRequest> detalleCompra;
}