package com.blumbit.eblumbit.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
@Entity 
public class Inventario {

    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(nullable = false)
    private Integer cantidadActual;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "almacen_id", referencedColumnName = "id")
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    private Producto producto;
}
