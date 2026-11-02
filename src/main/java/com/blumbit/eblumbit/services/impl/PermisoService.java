package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.CreatePermisoDto;
import com.blumbit.eblumbit.dto.PermisoDto;
import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.PermisoRepository;
import com.blumbit.eblumbit.services.spec.IPermisoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermisoService implements IPermisoService {

    private final PermisoRepository permisoRepository;

    @Override
    public List<PermisoDto> findAllPermisos() {
        return permisoRepository.findAll().stream()
                .map(PermisoDto::fromEntityData)
                .toList();
    }

    @Override
    public PermisoDto findPermisoById(Integer id) {
        return PermisoDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public PermisoDto createPermiso(CreatePermisoDto dto) {
        return PermisoDto.fromEntityData(permisoRepository.save(dto.toEntity()));
    }

    @Override
    @Transactional
    public PermisoDto updatePermiso(Integer id, CreatePermisoDto dto) {
        Permiso permiso = findEntity(id);
        permiso.setNombre(dto.getNombre());
        permiso.setDescripcion(dto.resolveDetalle());
        permiso.setSubject(dto.getSubject());
        permiso.setAction(dto.getAction());
        return PermisoDto.fromEntityData(permisoRepository.save(permiso));
    }

    @Override
    @Transactional
    public void deletePermiso(Integer id) {
        permisoRepository.delete(findEntity(id));
    }

    private Permiso findEntity(Integer id) {
        return permisoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", id));
    }
}
