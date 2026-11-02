package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.CategoriaDto;
import com.blumbit.eblumbit.dto.CreateCategoriaDto;

public interface ICategoriaService {

    List<CategoriaDto> findAllCategorias();

    CategoriaDto findCategoriaById(Integer id);

    CategoriaDto createCategoria(CreateCategoriaDto dto);

    CategoriaDto updateCategoria(Integer id, CreateCategoriaDto dto);

    void deleteCategoria(Integer id);
}
