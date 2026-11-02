package com.blumbit.eblumbit.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.blumbit.eblumbit.common.dto.PageableRequest;
import com.blumbit.eblumbit.common.dto.PageableResponse;
import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.dto.productos.ProductoQueryParams;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;
import com.blumbit.eblumbit.services.spec.IProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/productos" })
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    @GetMapping("/paginacion")
    public ResponseEntity<PageableResponse<ProductoResponse>> getProductosLegacy(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String filterValue,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String codigoBarra,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String nombreCategoria,
            @RequestParam(required = false) Integer almacenId) {
      ProductoFilterCriteria criteria = ProductoFilterCriteria.builder()
        .nombre(nombre)
        .descripcion(descripcion)
        .codigoBarra(codigoBarra)
        .marca(marca)
        .nombreCategoria(nombreCategoria)
        .almacenId(almacenId)
        .build();
        PageableRequest<ProductoFilterCriteria> pageableRequest = PageableRequest.<ProductoFilterCriteria>builder()
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .sortField(sortField)
        .sortOrder(sortOrder)
        .criterials(criteria)
        .build();
        return ResponseEntity.ok(productoService.getProductosPagination(pageableRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> getProductoById(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.findProductoById(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> createProducto(@ModelAttribute ProductoRequest request) {
        ProductoResponse created = productoService.createProducto(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> updateProducto(@PathVariable Integer id,
            @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.updateProducto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    private ProductoQueryParams buildQueryParams(int pageSize, int pageNumber, String search, Integer categoriaId,
            Boolean estado, String marca, String sortBy, boolean ascending) {
        return new ProductoQueryParams(pageSize, pageNumber, search, categoriaId, estado, marca, sortBy, ascending);
    }
    @GetMapping("/almacen/{id}")
    public ResponseEntity<List<ProductoResponse>> findProductosByAlmacen(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.getProductosByAlmacen(id));
    }
}
