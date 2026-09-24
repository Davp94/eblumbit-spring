package com.blumbit.eblumbit.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.CreateUsuarioDto;
import com.blumbit.eblumbit.dto.UsuarioDto;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.repository.UsuarioRepository;
import com.blumbit.eblumbit.validation.impl.UniqueNameChecker;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UniqueNameChecker uniqueNameChecker;

    public UsuarioService(UsuarioRepository usuarioRepository, UniqueNameChecker uniqueNameChecker) {
        this.usuarioRepository = usuarioRepository;
        this.uniqueNameChecker = uniqueNameChecker;
    }

    public List<UsuarioDto> findAllUsuarios() {
        List<UsuarioDto> usuariosDto = new ArrayList<>();
        for (Usuario u : usuarioRepository.findAll())
        {
            usuariosDto.add(UsuarioDto.fromEntityData(u));
        }
        return usuariosDto;
    }

    public UsuarioDto findUsuarioById(Integer id) {
        
        return UsuarioDto.fromEntityData(usuarioRepository.findById(id).orElse(null));
    }

    public UsuarioDto findUsuarioByUsername(String username) {
        return UsuarioDto.fromEntityData(usuarioRepository.findByUsername(username));
    }

    public UsuarioDto createUsuario(CreateUsuarioDto usuarioDto) {

        if(!uniqueNameChecker.isUniqueName(usuarioDto.getUsername()))
        {
            return null;
        }
        return UsuarioDto.fromEntityData(usuarioRepository.save(CreateUsuarioDto.toEntity(usuarioDto)));
    }

    public UsuarioDto updateUsuario(Integer id, CreateUsuarioDto usuarioDto) {
        Usuario usuarioFinded = usuarioRepository.findById(id).orElse(null);
        if(usuarioFinded != null)
        {
            usuarioFinded.setUsername(usuarioDto.getUsername());
            usuarioFinded.setPassword(usuarioDto.getPassword());
            usuarioFinded.setEmail(usuarioDto.getCorreo());
            return UsuarioDto.fromEntityData(usuarioRepository.save(usuarioFinded));
        }
        return null;
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public void logicalDeleteUsuario(Integer id) {
        Usuario usuarioFinded = usuarioRepository.findById(id).orElse(null);
        if(usuarioFinded != null)
        {
            usuarioFinded.setEstado(false);
            usuarioRepository.save(usuarioFinded);
        }
    }
}
