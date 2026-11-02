package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.CreateProveedorDto;
import com.blumbit.eblumbit.dto.ProveedorDto;
import com.blumbit.eblumbit.entities.Proveedor;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.ProveedorRepository;
import com.blumbit.eblumbit.services.spec.IProveedorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public List<ProveedorDto> findAllProveedores() {
        return proveedorRepository.findAll().stream()
                .map(ProveedorDto::fromEntityData)
                .toList();
    }

    @Override
    public ProveedorDto findProveedorById(Integer id) {
        return ProveedorDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public ProveedorDto createProveedor(CreateProveedorDto dto) {
        return ProveedorDto.fromEntityData(proveedorRepository.save(dto.toEntity()));
    }

    @Override
    @Transactional
    public ProveedorDto updateProveedor(Integer id, CreateProveedorDto dto) {
        Proveedor proveedor = findEntity(id);
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNroIdentificacion(dto.getNroIdentificacion());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setObservaciones(dto.getObservaciones());
        return ProveedorDto.fromEntityData(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public void deleteProveedor(Integer id) {
        proveedorRepository.delete(findEntity(id));
    }

    private Proveedor findEntity(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", id));
    }
}
