package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.ventas.VentasDetailResponse;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;

public interface IVentasService {

    List<VentasResponse> findAllVentas();

    VentasDetailResponse findVentaById(Integer id);

    VentasResponse createVenta(VentasRequest ventasRequest);

    void anularVenta(Integer id);

    byte[] generateReport(Integer id);
}
