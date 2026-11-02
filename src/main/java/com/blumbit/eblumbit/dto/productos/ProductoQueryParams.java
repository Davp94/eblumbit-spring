package com.blumbit.eblumbit.dto.productos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoQueryParams {
    private int pageSize = 10;
    private int pageNumber = 1;
    private String search;
    private Integer categoriaId;
    private Boolean estado;
    private String marca;
    private String sortBy = "Id";
    private boolean ascending = true;
}
