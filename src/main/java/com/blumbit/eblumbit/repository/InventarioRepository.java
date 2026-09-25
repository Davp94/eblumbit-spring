package com.blumbit.eblumbit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blumbit.eblumbit.entities.Inventario;
import java.util.List;


public interface InventarioRepository extends JpaRepository<Inventario, Integer>{

    Optional<Inventario> findByAlmacenIdAndProductoId(Integer almacenId, Integer productoId);
}
