package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.ventas.AnularVentaDto;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;
import com.blumbit.eblumbit.services.spec.IVentasService;

@Service
public class VentasMockService implements IVentasService {

    @Override
    public List<VentasResponse> findAllVentas() {

        return List.of();
        // TODO Auto-generated method stub
    }

    @Override
    public VentasResponse findVentaById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findVentaById'");
    }

    @Override
    public VentasResponse createVenta(VentasRequest ventasRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createVenta'");
    }

    @Override
    public void anularVenta(Integer id, AnularVentaDto anularVentaDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'anularVenta'");
    }

}
