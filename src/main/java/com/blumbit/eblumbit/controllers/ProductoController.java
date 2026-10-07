package com.blumbit.eblumbit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.common.dto.PageableRequest;
import com.blumbit.eblumbit.common.dto.PageableResponse;
import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.dto.productos.ProductoRequest;
import com.blumbit.eblumbit.dto.productos.ProductoResponse;
import com.blumbit.eblumbit.services.spec.IProductoService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping("/productos")
@RequiredArgsConstructor 
public class ProductoController {

    private final IProductoService productoService;

    @GetMapping("/paginacion")
    public ResponseEntity<PageableResponse<ProductoResponse>> getProductosPagination(
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "1") Integer pageNumber,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "asc") String sortOrder,
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

    @PostMapping
    public ResponseEntity<ProductoResponse> createProducto(@ModelAttribute ProductoRequest request) {
        
        return ResponseEntity.ok(productoService.createProducto(request));
    }


    @GetMapping("/almacen/{id}")
    public ResponseEntity<List<ProductoResponse>> findProductosByAlmacen(@PathVariable(name = "id")  Integer idAlmacen) {
        return ResponseEntity.ok(productoService.getProductosByAlmacen(idAlmacen));
    }
    
    
    
}
