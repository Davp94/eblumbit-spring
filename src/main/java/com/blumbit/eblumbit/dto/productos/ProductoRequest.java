package com.blumbit.eblumbit.dto.productos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.blumbit.eblumbit.entities.Producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contrato de productos compartido con .NET.
 * Acepta el alias legado "codigoBarras" ademas de "codigoBarra".
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    private String nombre;
    private String codigoBarra;
    private String codigoBarras;
    private String unidadMedida;
    private String marca;
    private MultipartFile imagen;
    private String descripcion;
    private BigDecimal precioVentaActual;
    private Integer stockMinimo;
    private Boolean estado;
    private Integer categoriaId;

    public String resolveCodigoBarra() {
        return codigoBarra != null ? codigoBarra : codigoBarras;
    }

    public static Producto toEntity(ProductoRequest request) {
        return Producto.builder()
                .nombre(request.getNombre())
                .codigoBarra(request.resolveCodigoBarra())
                .unidadMedida(request.getUnidadMedida())
                .marca(request.getMarca())
                .descripcion(request.getDescripcion())
                .precioVentaActual(request.getPrecioVentaActual())
                .stockMinimo(request.getStockMinimo())
                .estado(request.getEstado() == null ? Boolean.TRUE : request.getEstado())
                .build();
    }
}
