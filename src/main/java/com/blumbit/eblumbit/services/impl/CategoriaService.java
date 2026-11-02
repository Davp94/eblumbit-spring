package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.CategoriaDto;
import com.blumbit.eblumbit.dto.CreateCategoriaDto;
import com.blumbit.eblumbit.entities.Categorias;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.CategoriaRepository;
import com.blumbit.eblumbit.services.spec.ICategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaDto> findAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaDto::fromEntityData)
                .toList();
    }

    @Override
    public CategoriaDto findCategoriaById(Integer id) {
        return CategoriaDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public CategoriaDto createCategoria(CreateCategoriaDto dto) {
        return CategoriaDto.fromEntityData(categoriaRepository.save(dto.toEntity()));
    }

    @Override
    @Transactional
    public CategoriaDto updateCategoria(Integer id, CreateCategoriaDto dto) {
        Categorias categoria = findEntity(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.resolveDetalle());
        return CategoriaDto.fromEntityData(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void deleteCategoria(Integer id) {
        Categorias categoria = findEntity(id);
        categoriaRepository.delete(categoria);
    }

    private Categorias findEntity(Integer id) {
        return categoriaRepository.findById(id.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }
}
