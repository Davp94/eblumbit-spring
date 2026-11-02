package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.CreateProveedorDto;
import com.blumbit.eblumbit.dto.ProveedorDto;

public interface IProveedorService {

    List<ProveedorDto> findAllProveedores();

    ProveedorDto findProveedorById(Integer id);

    ProveedorDto createProveedor(CreateProveedorDto dto);

    ProveedorDto updateProveedor(Integer id, CreateProveedorDto dto);

    void deleteProveedor(Integer id);
}
