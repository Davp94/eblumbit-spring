package com.blumbit.eblumbit.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
import com.blumbit.eblumbit.dto.productos.ProductoQueryParams;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;
import com.blumbit.eblumbit.entities.Categorias;
import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.CategoriaRepository;
import com.blumbit.eblumbit.repository.InventarioRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.specification.ProductoSpecification;
import com.blumbit.eblumbit.services.spec.IFileService;
import com.blumbit.eblumbit.services.spec.IProductoService;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;
    private final IFileService fileService;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PageableResponse<ProductoResponse> getProductosPagination(PageableRequest<ProductoFilterCriteria> pageableRequest) {
       
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
            .items(productoPage.getContent().stream().map((ProductoResponse::fromEntity)).toList())
            .pageNumber(productoPage.getNumber())
            .pageSize(productoPage.getSize())
            .totalElements(productoPage.getTotalElements())
            .totalPages(productoPage.getTotalPages())
            .build();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public ProductoResponse findProductoById(Integer id) {
        return ProductoResponse.fromEntity(findEntity(id));
    }

    @Override
    @Transactional
    public ProductoResponse createProducto(ProductoRequest request) {
        if (request.getImagen() == null || request.getImagen().isEmpty()) {
            throw new ResourceNotFoundException("Archivo es nulo o esta vacío");
        }
        String filePath = fileService.createFile(request.getImagen());
        Producto producto = ProductoRequest.toEntity(request);
        producto.setEstado(request.getEstado() == null ? Boolean.TRUE : request.getEstado());
        producto.setFechaRegistro(LocalDateTime.now(ZoneOffset.UTC).truncatedTo(java.time.temporal.ChronoUnit.MICROS));
        producto.setImagen(filePath);
        producto.setCategoria(findCategoria(request.getCategoriaId()));
        return ProductoResponse.fromEntity(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse updateProducto(Integer id, ProductoRequest request) {
        Producto producto = findEntity(id);
        producto.setNombre(request.getNombre());
        producto.setCodigoBarra(request.resolveCodigoBarra());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setMarca(request.getMarca());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioVentaActual(request.getPrecioVentaActual());
        producto.setStockMinimo(request.getStockMinimo());
        if (request.getEstado() != null) {
            producto.setEstado(request.getEstado());
        }
        producto.setCategoria(findCategoria(request.getCategoriaId()));
        return ProductoResponse.fromEntity(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void deleteProducto(Integer id) {
        productoRepository.delete(findEntity(id));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<ProductoResponse> getProductosByAlmacen(Integer almacenId) {
        return inventarioRepository.findByAlmacen_Id(almacenId).stream()
                .map(i -> ProductoResponse.fromEntity(i.getProducto()))
                .collect(Collectors.toList());
    }

    private Producto findEntity(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    private Categorias findCategoria(Integer categoriaId) {
        if (categoriaId == null) {
            throw new ResourceNotFoundException("Categoria con id null no encontrado");
        }
        return categoriaRepository.findById(categoriaId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", categoriaId));
    }

    private Sort resolveSort(String sortBy, boolean ascending) {
        String field = switch (sortBy == null ? "id" : sortBy.trim().toLowerCase()) {
            case "nombre" -> "nombre";
            case "precio" -> "precioVentaActual";
            case "fecha_registro" -> "fechaRegistro";
            default -> "id";
        };
        return ascending ? Sort.by(field).ascending() : Sort.by(field).descending();
    }
}
