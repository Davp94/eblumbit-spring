package com.blumbit.eblumbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blumbit.eblumbit.entities.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
}
