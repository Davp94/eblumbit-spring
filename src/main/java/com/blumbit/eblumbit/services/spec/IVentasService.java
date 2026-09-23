package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.ventas.AnularVentaDto;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;

public interface IVentasService {

    List<VentasResponse> findAllVentas();

    VentasResponse findVentaById(Integer id);

    VentasResponse createVenta(VentasRequest ventasRequest);

    void anularVenta(Integer id, AnularVentaDto anularVentaDto);

}
