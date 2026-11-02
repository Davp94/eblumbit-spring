package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.AlmacenDto;
import com.blumbit.eblumbit.dto.CreateAlmacenDto;
import com.blumbit.eblumbit.entities.Almacen;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.AlmacenRepository;
import com.blumbit.eblumbit.repository.SucursalRepository;
import com.blumbit.eblumbit.services.spec.IAlmacenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlmacenService implements IAlmacenService {

    private final AlmacenRepository almacenRepository;
    private final SucursalRepository sucursalRepository;

    @Override
    public List<AlmacenDto> findAllAlmacenes() {
        return almacenRepository.findAll().stream()
                .map(AlmacenDto::fromEntityData)
                .toList();
    }

    @Override
    public AlmacenDto findAlmacenById(Integer id) {
        return AlmacenDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public AlmacenDto createAlmacen(CreateAlmacenDto dto) {
        Almacen almacen = Almacen.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .sucursal(sucursalRepository.findById(dto.getSucursalId().longValue())
                        .orElseThrow(() -> new ResourceNotFoundException("Sucursal", dto.getSucursalId())))
                .build();
        return AlmacenDto.fromEntityData(almacenRepository.save(almacen));
    }

    @Override
    @Transactional
    public AlmacenDto updateAlmacen(Integer id, CreateAlmacenDto dto) {
        Almacen almacen = findEntity(id);
        almacen.setCodigo(dto.getCodigo());
        almacen.setNombre(dto.getNombre());
        almacen.setDescripcion(dto.getDescripcion());
        almacen.setSucursal(sucursalRepository.findById(dto.getSucursalId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", dto.getSucursalId())));
        return AlmacenDto.fromEntityData(almacenRepository.save(almacen));
    }

    @Override
    @Transactional
    public void deleteAlmacen(Integer id) {
        almacenRepository.delete(findEntity(id));
    }

    private Almacen findEntity(Integer id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Almacen", id));
    }
}
