package com.blumbit.eblumbit.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.blumbit.eblumbit.services.UsuarioService;

@RestController
@RequestMapping("/usuarios") 
public class UsuarioController {

    @Autowired 
    private UsuarioService usuarioService;

    @GetMapping 
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.findUsuarioById(id));
    }

    @PostMapping 
    public ResponseEntity<UsuarioDto> createUsuario(@RequestBody CreateUsuarioDto usuario) {
        UsuarioDto userSaved = usuarioService.createUsuario(usuario);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userSaved.getId())
                    .toUri();
        return ResponseEntity.created(location).body(userSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(@PathVariable Integer id, @RequestBody CreateUsuarioDto usuario) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> logicalDeleteusuario(@PathVariable Integer id) {
        usuarioService.logicalDeleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
