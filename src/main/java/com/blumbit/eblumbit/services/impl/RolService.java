package com.blumbit.eblumbit.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.CreateRolDto;
import com.blumbit.eblumbit.dto.RolDto;
import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.entities.Rol;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.PermisoRepository;
import com.blumbit.eblumbit.repository.RolRepository;
import com.blumbit.eblumbit.repository.RolUsuarioRepository;
import com.blumbit.eblumbit.services.spec.IRolService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolService implements IRolService {

    private final RolRepository rolRepository;

    private final PermisoRepository permisoRepository;

    private final RolUsuarioRepository rolUsuarioRepository;

    @Override
    public List<RolDto> findAllRoles() {
        return rolRepository.findAll().stream()
                .map(RolDto::fromEntityData)
                .toList();
    }

    @Override
    public RolDto findRolById(Integer id) {
        return RolDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public RolDto createRol(CreateRolDto dto) {
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        rol.setPermisos(resolvePermisos(dto.getPermisosIds()));
        return RolDto.fromEntityData(rolRepository.save(rol));
    }

    @Override
    @Transactional
    public RolDto updateRol(Integer id, CreateRolDto dto) {
        Rol rol = findEntity(id);
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        rol.setPermisos(resolvePermisos(dto.getPermisosIds()));
        return RolDto.fromEntityData(rolRepository.save(rol));
    }

    @Override
    @Transactional
    public void deleteRol(Integer id) {
        Rol rol = findEntity(id);
        rolUsuarioRepository.deleteByRol_Id(id);
        rolRepository.delete(rol);
    }

    private Rol findEntity(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", id));
    }

    private List<Permiso> resolvePermisos(List<Integer> permisosIds) {
        if (permisosIds == null || permisosIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> distinctIds = permisosIds.stream().distinct().toList();
        List<Permiso> permisos = permisoRepository.findAllById(distinctIds);
        if (permisos.size() != distinctIds.size()) {
            throw new BusinessRuleException("Uno o más permisos no existen");
        }
        return new ArrayList<>(permisos);
    }
}
