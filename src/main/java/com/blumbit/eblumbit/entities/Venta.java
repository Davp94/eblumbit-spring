package com.blumbit.eblumbit.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(scale = 12, precision = 2)
    private BigDecimal descuentoTotal;

    @Column(nullable = false)
    private Boolean estado;

    @Column(length = Integer.MAX_VALUE)
    private String detalle;

    @ManyToOne 
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne 
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

}
