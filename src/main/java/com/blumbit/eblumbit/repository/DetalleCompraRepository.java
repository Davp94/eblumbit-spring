package com.blumbit.eblumbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blumbit.eblumbit.entities.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    List<DetalleCompra> findByCompra_Id(Integer compraId);
}