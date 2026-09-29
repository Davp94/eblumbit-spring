package com.blumbit.eblumbit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "razon_social", length = 255, nullable = false)
    private String razonSocial;

    @Column(name = "nro_identificacion", length = 30)
    private String nroIdentificacion;

    @Column(length = 100, nullable = false)
    private String contacto;

    @Column(length = 20, nullable = false)
    private String telefono;

    @Column(length = 150)
    private String correo;

    @Column(columnDefinition = "text")
    private String observaciones;

    private Boolean estado;
}