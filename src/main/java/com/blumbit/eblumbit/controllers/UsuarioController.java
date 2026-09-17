package com.blumbit.eblumbit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.services.UsuarioService;

@RestController
@RequestMapping("/usuarios") 
public class UsuarioController {

    @Autowired 
    private UsuarioService usuarioService;

    @GetMapping 
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario findUsuarioById(@PathVariable Integer id) {
        return usuarioService.findUsuarioById(id);
    }

    @PostMapping 
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.createUsuario(usuario);
    }

    @PutMapping 
    public Usuario updateUsuario(@RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(usuario);
    }

    @PatchMapping("/{id}")
    public void logicalDeleteusuario(@PathVariable Integer id) {
        usuarioService.logicalDeleteUsuario(id);
    }

    @DeleteMapping("/{id}") 
    public void deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
    }
}
