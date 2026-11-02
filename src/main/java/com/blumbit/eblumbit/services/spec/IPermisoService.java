package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.CreatePermisoDto;
import com.blumbit.eblumbit.dto.PermisoDto;

public interface IPermisoService {

    List<PermisoDto> findAllPermisos();

    PermisoDto findPermisoById(Integer id);

    PermisoDto createPermiso(CreatePermisoDto dto);

    PermisoDto updatePermiso(Integer id, CreatePermisoDto dto);

    void deletePermiso(Integer id);
}
