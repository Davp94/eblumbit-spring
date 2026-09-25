package com.blumbit.eblumbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blumbit.eblumbit.entities.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer>{

    List<DetalleVenta> findByVenta_Id(Integer id);
}
