package com.blumbit.eblumbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blumbit.eblumbit.entities.RolUsuario;

@Repository
public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    List<RolUsuario> findByUsuario_Id(Integer usuarioId);

    void deleteByUsuario_Id(Integer usuarioId);
}
