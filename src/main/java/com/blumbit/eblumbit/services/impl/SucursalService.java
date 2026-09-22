package com.blumbit.eblumbit.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.CreateSucursalDto;
import com.blumbit.eblumbit.dto.SucursalDto;
import com.blumbit.eblumbit.entities.Sucursal;
import com.blumbit.eblumbit.repository.SucursalRepository;
import com.blumbit.eblumbit.services.spec.ISucursalService;

@Service 
public class SucursalService implements ISucursalService {

    @Autowired 
    private SucursalRepository sucursalRepository;

    @Override
    public List<SucursalDto> findAllSucursales() {
        return sucursalRepository.findAll().stream()
        .map(SucursalDto::fromEntityData).collect(Collectors.toList());
    }

    @Override
    public SucursalDto findSucursalById(Integer id) {
        return SucursalDto.fromEntityData(sucursalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada")));
    }

    @Override
    public SucursalDto createSucursal(CreateSucursalDto createSucursalDto) {
        return SucursalDto.fromEntityData(sucursalRepository.save(CreateSucursalDto.toEntity(createSucursalDto)));
    }

    @Override
    public SucursalDto updateSucursal(Integer id, CreateSucursalDto createSucursalDto) {
       Sucursal sucursal = sucursalRepository.findById(id).orElseThrow(()->new RuntimeException("sucursal no encontrada"));
        sucursal.setCiudad(createSucursalDto.getCiudad());
        sucursal.setDireccion(createSucursalDto.getDireccion());
        sucursal.setNombre(createSucursalDto.getNombre());
        sucursal.setTelefono(createSucursalDto.getTelefono());
       return SucursalDto.fromEntityData(sucursalRepository.save(CreateSucursalDto.toEntity(createSucursalDto)));
    }

    @Override
    public void deleteSucursal(Integer id) {
        sucursalRepository.deleteById(id);
    }

}
