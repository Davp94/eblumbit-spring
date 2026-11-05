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

import com.blumbit.eblumbit.dto.CreateRolDto;
import com.blumbit.eblumbit.dto.RolDto;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.services.spec.IRolService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {

    private final IRolService rolService;

    @GetMapping
    public ResponseEntity<List<RolDto>> getAllRoles() {
        return ResponseEntity.ok(rolService.findAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> getRolById(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.findRolById(id));
    }

    @PostMapping
    public ResponseEntity<RolDto> createRol(@Valid @RequestBody CreateRolDto dto) {
        RolDto created = rolService.createRol(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDto> updateRol(@PathVariable Integer id, @Valid @RequestBody CreateRolDto dto) {
        return ResponseEntity.ok(rolService.updateRol(id, dto));
    }

    @PutMapping
    public ResponseEntity<RolDto> updateRolFromBody(@Valid @RequestBody CreateRolDto dto) {
        if (dto.getId() == null) {
            throw new BusinessRuleException("El id del rol es obligatorio");
        }
        return ResponseEntity.ok(rolService.updateRol(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Integer id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }
}
