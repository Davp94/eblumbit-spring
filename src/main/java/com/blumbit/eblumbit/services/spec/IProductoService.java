package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.common.dto.PageableRequest;
import com.blumbit.eblumbit.common.dto.PageableResponse;
import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;

public interface IProductoService {

    PageableResponse<ProductoResponse> getProductosPagination(PageableRequest<ProductoFilterCriteria> pageableRequest);

    ProductoResponse createProducto(ProductoRequest request);

    List<ProductoResponse> getProductosByAlmacen(Integer almacenId);
}
