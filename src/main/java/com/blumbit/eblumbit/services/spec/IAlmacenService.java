package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.AlmacenDto;
import com.blumbit.eblumbit.dto.CreateAlmacenDto;

public interface IAlmacenService {

    List<AlmacenDto> findAllAlmacenes();

    AlmacenDto findAlmacenById(Integer id);

    AlmacenDto createAlmacen(CreateAlmacenDto dto);

    AlmacenDto updateAlmacen(Integer id, CreateAlmacenDto dto);

    void deleteAlmacen(Integer id);
}
