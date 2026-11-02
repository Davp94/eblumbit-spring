package com.blumbit.eblumbit.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.blumbit.eblumbit.dto.CreateProveedorDto;
import com.blumbit.eblumbit.dto.ProveedorDto;
import com.blumbit.eblumbit.services.spec.IProveedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final IProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorDto>> getAllProveedores() {
        return ResponseEntity.ok(proveedorService.findAllProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDto> getProveedorById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.findProveedorById(id));
    }

    @PostMapping
    public ResponseEntity<ProveedorDto> createProveedor(@Valid @RequestBody CreateProveedorDto dto) {
        ProveedorDto created = proveedorService.createProveedor(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDto> updateProveedor(@PathVariable Integer id, @Valid @RequestBody CreateProveedorDto dto) {
        return ResponseEntity.ok(proveedorService.updateProveedor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Integer id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
