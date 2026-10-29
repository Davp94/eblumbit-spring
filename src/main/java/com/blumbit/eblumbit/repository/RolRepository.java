package com.blumbit.eblumbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blumbit.eblumbit.entities.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
}
