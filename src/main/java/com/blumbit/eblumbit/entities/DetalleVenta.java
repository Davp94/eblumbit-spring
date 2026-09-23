package com.blumbit.eblumbit.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(scale = 12, precision = 2, nullable = false)
    private BigDecimal precioUnitarioVenta;

    @Column(length = Integer.MAX_VALUE)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "venta_id", referencedColumnName = "id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "almacen_id", referencedColumnName = "id")
    private Almacen almacen;
}
