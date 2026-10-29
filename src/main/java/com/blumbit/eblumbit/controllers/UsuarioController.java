package com.blumbit.eblumbit.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.blumbit.eblumbit.dto.CreateUsuarioDto;
import com.blumbit.eblumbit.dto.UsuarioDto;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/usuarios", "/api/usuarios", "/users", "/api/users" })
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('leer:users')")
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAllUsuarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('leer:users')")
    public ResponseEntity<UsuarioDto> findUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.findUsuarioById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('crear:users')")
    public ResponseEntity<UsuarioDto> createUsuario(@Valid @RequestBody CreateUsuarioDto usuario) {
        UsuarioDto userSaved = usuarioService.createUsuario(usuario);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userSaved.getId())
                    .toUri();
        return ResponseEntity.created(location).body(userSaved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('actualizar:users')")
    public ResponseEntity<UsuarioDto> updateUsuario(@PathVariable Integer id, @Valid @RequestBody CreateUsuarioDto usuario) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, usuario));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('actualizar:users')")
    public ResponseEntity<UsuarioDto> updateUsuarioFromBody(@Valid @RequestBody CreateUsuarioDto usuario) {
        if (usuario.getId() == null) {
            throw new BusinessRuleException("El id del usuario es obligatorio");
        }
        return ResponseEntity.ok(usuarioService.updateUsuario(usuario.getId(), usuario));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('eliminar:users')")
    public ResponseEntity<Void> logicalDeleteusuario(@PathVariable Integer id) {
        usuarioService.logicalDeleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('eliminar:users')")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
