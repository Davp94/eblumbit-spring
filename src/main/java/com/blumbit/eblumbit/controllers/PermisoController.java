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

import com.blumbit.eblumbit.dto.CreatePermisoDto;
import com.blumbit.eblumbit.dto.PermisoDto;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.services.spec.IPermisoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/permisos")
@RequiredArgsConstructor
public class PermisoController {

    private final IPermisoService permisoService;

    @GetMapping
    public ResponseEntity<List<PermisoDto>> getAllPermisos() {
        return ResponseEntity.ok(permisoService.findAllPermisos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermisoDto> getPermisoById(@PathVariable Integer id) {
        return ResponseEntity.ok(permisoService.findPermisoById(id));
    }

    @PostMapping
    public ResponseEntity<PermisoDto> createPermiso(@Valid @RequestBody CreatePermisoDto dto) {
        PermisoDto created = permisoService.createPermiso(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermisoDto> updatePermiso(@PathVariable Integer id, @Valid @RequestBody CreatePermisoDto dto) {
        return ResponseEntity.ok(permisoService.updatePermiso(id, dto));
    }

    @PutMapping
    public ResponseEntity<PermisoDto> updatePermisoFromBody(@Valid @RequestBody CreatePermisoDto dto) {
        if (dto.getId() == null) {
            throw new BusinessRuleException("El id del permiso es obligatorio");
        }
        return ResponseEntity.ok(permisoService.updatePermiso(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermiso(@PathVariable Integer id) {
        permisoService.deletePermiso(id);
        return ResponseEntity.noContent().build();
    }
}
