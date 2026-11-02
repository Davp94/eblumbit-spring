package com.blumbit.eblumbit.dto.ventas;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaRequest {

    private Integer cantidad;

    private BigDecimal precioUnitarioVenta;

    private String observaciones;

    private Integer productoId;

    private Integer almacenId;

    public String resolveObservaciones() {
        return observaciones != null ? observaciones : "" ;
    }
}
