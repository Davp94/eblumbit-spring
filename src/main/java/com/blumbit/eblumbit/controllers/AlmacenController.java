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

import com.blumbit.eblumbit.dto.AlmacenDto;
import com.blumbit.eblumbit.dto.CreateAlmacenDto;
import com.blumbit.eblumbit.services.spec.IAlmacenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/almacenes", "/almacenes" })
@RequiredArgsConstructor
public class AlmacenController {

    private final IAlmacenService almacenService;

    @GetMapping
    public ResponseEntity<List<AlmacenDto>> getAllAlmacenes() {
        return ResponseEntity.ok(almacenService.findAllAlmacenes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDto> getAlmacenById(@PathVariable Integer id) {
        return ResponseEntity.ok(almacenService.findAlmacenById(id));
    }

    @PostMapping
    public ResponseEntity<AlmacenDto> createAlmacen(@Valid @RequestBody CreateAlmacenDto dto) {
        AlmacenDto created = almacenService.createAlmacen(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDto> updateAlmacen(@PathVariable Integer id,
            @Valid @RequestBody CreateAlmacenDto dto) {
        return ResponseEntity.ok(almacenService.updateAlmacen(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlmacen(@PathVariable Integer id) {
        almacenService.deleteAlmacen(id);
        return ResponseEntity.noContent().build();
    }
}
