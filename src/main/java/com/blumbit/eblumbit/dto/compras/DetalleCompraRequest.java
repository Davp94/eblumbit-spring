package com.blumbit.eblumbit.dto.compras;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleCompraRequest {

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    private BigDecimal precioUnitarioCompra;

    private String observaciones;

    @NotNull
    private Integer productoId;

    @NotNull
    private Integer almacenId;
}