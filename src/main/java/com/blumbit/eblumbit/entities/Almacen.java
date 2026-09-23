package com.blumbit.eblumbit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "almacenes")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100)
    private String codigo;

    @Column(length = Integer.MAX_VALUE)
    private String descripcion;

    @ManyToOne 
    @JoinColumn(name = "sucursal_id", referencedColumnName = "id")
    private Sucursal sucursal;
}
