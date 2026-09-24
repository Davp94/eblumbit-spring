package com.blumbit.eblumbit.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.repository.UsuarioRepository;
import com.blumbit.eblumbit.validation.spec.IUniqueNameChecker;

@Service 
public class UniqueNameChecker implements IUniqueNameChecker{

    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean isUniqueName(String name) {
        return !usuarioRepository.existByUsername(name);
    }

}
