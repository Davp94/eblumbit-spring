package com.blumbit.eblumbit.dto.compras;

import java.math.BigDecimal;

import com.blumbit.eblumbit.entities.DetalleCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleCompraResponse {

    private Integer id;
    private Integer compraId;
    private Integer productoId;
    private Integer almacenId;
    private Integer cantidad;
    private BigDecimal precioUnitarioCompra;
    private String observaciones;

    public static DetalleCompraResponse fromEntity(DetalleCompra detalleCompra) {
        return DetalleCompraResponse.builder()
            .id(detalleCompra.getId())
            .compraId(detalleCompra.getCompra().getId())
            .productoId(detalleCompra.getProducto().getId())
            .almacenId(detalleCompra.getAlmacen().getId())
            .cantidad(detalleCompra.getCantidad())
            .precioUnitarioCompra(detalleCompra.getPrecioUnitarioCompra())
            .observaciones(detalleCompra.getObservaciones())
            .build();
    }
}