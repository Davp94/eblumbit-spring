package com.blumbit.eblumbit.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.dto.compras.ComprasRequest;
import com.blumbit.eblumbit.dto.compras.ComprasResponse;
import com.blumbit.eblumbit.services.spec.IComprasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class ComprasController {

    private final IComprasService comprasService;

    @GetMapping
    public ResponseEntity<List<ComprasResponse>> findAllCompras() {
        return ResponseEntity.ok(comprasService.findAllCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprasResponse> findCompraById(@PathVariable Integer id) {
        return ResponseEntity.ok(comprasService.findCompraById(id));
    }

    @PostMapping
    public ResponseEntity<ComprasResponse> createCompra(@Valid @RequestBody ComprasRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comprasService.createCompra(request));
    }
}