package com.blumbit.eblumbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blumbit.eblumbit.entities.Usuario;

@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //QUERY METHODS
    List<Usuario> findByCorreo(String correo); // select * from users where correo = ${correo}

    Usuario findByUsername(String username); // select * from users where username = ${username} limit 1
}
