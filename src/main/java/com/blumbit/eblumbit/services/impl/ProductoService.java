package com.blumbit.eblumbit.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.common.dto.PageableRequest;
import com.blumbit.eblumbit.common.dto.PageableResponse;
import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;
import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.repository.CategoriaRepository;
import com.blumbit.eblumbit.repository.InventarioRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.specification.ProductoSpecification;
import com.blumbit.eblumbit.services.spec.IFileService;
import com.blumbit.eblumbit.services.spec.IProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ProductoService implements IProductoService{

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;
    private final IFileService fileService;

    @Override
    public PageableResponse<ProductoResponse> getProductosPagination(
            PageableRequest<ProductoFilterCriteria> pageableRequest) {
        
            Sort sort = pageableRequest.getSortOrder().equalsIgnoreCase("asc")
            ? Sort.by(pageableRequest.getSortField()).ascending()
            : Sort.by(pageableRequest.getSortField()).descending();

            Pageable pageable = PageRequest.of(pageableRequest.getPageNumber(), pageableRequest.getPageSize(), sort);

            Specification<Producto> specification = null;
            if(pageableRequest.getCriterials() != null) {
                specification = ProductoSpecification.createSpecification(pageableRequest.getFilterValue(), pageableRequest.getCriterials());
            }

            Page<Producto> productoPage = productoRepository.findAll(specification, pageable);

            return PageableResponse.<ProductoResponse>builder()
            .content(productoPage.getContent().stream().map((ProductoResponse::fromEntity)).toList())
            .pageNumber(productoPage.getNumber())
            .pageSize(productoPage.getSize())
            .totalElements(productoPage.getTotalElements())
            .totalPages(productoPage.getTotalPages())
            .build();
    }

    @Override
    public ProductoResponse createProducto(ProductoRequest request) {

        String filePath= fileService.createFile(request.getImagen());
        Producto productoToCreate = ProductoRequest.toEntity(request);
        productoToCreate.setEstado(true);
        productoToCreate.setFechaRegistro(LocalDateTime.now());
        productoToCreate.setImagen(filePath);
        productoToCreate.setCategoria(categoriaRepository.findById(request.getCategoriaId())
        .orElseThrow(()-> new RuntimeException("Categoria no encontrada")));
        return ProductoResponse.fromEntity(productoRepository.save(productoToCreate));
    }

    @Override
    public List<ProductoResponse> getProductosByAlmacen(Integer almacenId) {
        return inventarioRepository.findByAlmacen_Id(almacenId).stream()
        .map(i->ProductoResponse.fromEntity(i.getProducto())).collect(Collectors.toList());
    }

}
