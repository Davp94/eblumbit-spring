package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.compras.ComprasRequest;
import com.blumbit.eblumbit.dto.compras.ComprasResponse;

public interface IComprasService {

    List<ComprasResponse> findAllCompras();

    ComprasResponse findCompraById(Integer id);

    ComprasResponse createCompra(ComprasRequest comprasRequest);
}