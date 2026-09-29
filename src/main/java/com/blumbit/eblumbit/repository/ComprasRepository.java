package com.blumbit.eblumbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blumbit.eblumbit.entities.Compra;

public interface ComprasRepository extends JpaRepository<Compra, Integer> {
}