package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.CreateRolDto;
import com.blumbit.eblumbit.dto.RolDto;

public interface IRolService {

    List<RolDto> findAllRoles();

    RolDto findRolById(Integer id);

    RolDto createRol(CreateRolDto dto);

    RolDto updateRol(Integer id, CreateRolDto dto);

    void deleteRol(Integer id);
}
