package com.blumbit.eblumbit.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll(); // select * from users
    }

    public Usuario findUsuarioById(Integer id) {
        return usuarioRepository.findById(id).orElse(null); // select * from users where id = ?
    }

    public Usuario findUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
