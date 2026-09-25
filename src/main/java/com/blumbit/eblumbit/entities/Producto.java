package com.blumbit.eblumbit.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
public class Producto {

    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(length = 200, nullable = false) 
    private String nombre;

    @Column(length = Integer.MAX_VALUE) 
    private String descripcion;

    @Column(length = 100)
    private String codigoBarra;  //codigo_barra

    @Column(length = 50)
    private String unidadMedida;

    @Column(length = 100)
    private String marca;

    @Column (scale = 12, precision = 2)
    private BigDecimal precioVentaActual;

    private Integer stockMinimo;

    @Column(length = 255)
    private String imagen;

    @Column(nullable = false) 
    private Boolean estado;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne 
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categorias categoria;
}
