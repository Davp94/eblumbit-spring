package com.blumbit.eblumbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blumbit.eblumbit.entities.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}