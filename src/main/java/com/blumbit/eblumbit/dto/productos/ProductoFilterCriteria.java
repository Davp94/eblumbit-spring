package com.blumbit.eblumbit.dto.productos;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class ProductoFilterCriteria {
    private String nombre;
    private String descripcion;
    private String codigoBarra;
    private String marca;
    private String nombreCategoria;
    private Integer almacenId;
}
