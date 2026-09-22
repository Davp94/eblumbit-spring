package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.CreateSucursalDto;
import com.blumbit.eblumbit.dto.SucursalDto;

public interface ISucursalService {

    List<SucursalDto> findAllSucursales();

    SucursalDto findSucursalById(Integer id);

    SucursalDto createSucursal(CreateSucursalDto createSucursalDto);
   
    SucursalDto updateSucursal(Integer id, CreateSucursalDto createSucursalDto);

    void deleteSucursal(Integer id);


}
