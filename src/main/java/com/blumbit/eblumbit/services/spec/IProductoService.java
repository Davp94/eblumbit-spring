package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.common.dto.PageableRequest;
import com.blumbit.eblumbit.common.dto.PageableResponse;
import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;

public interface IProductoService {

    PageableResponse<ProductoResponse> getProductosPagination(PageableRequest<ProductoFilterCriteria> queryParams);

    ProductoResponse findProductoById(Integer id);

    ProductoResponse createProducto(ProductoRequest request);

    ProductoResponse updateProducto(Integer id, ProductoRequest request);

    void deleteProducto(Integer id);

    List<ProductoResponse> getProductosByAlmacen(Integer almacenId);
}
