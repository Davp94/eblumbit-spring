package com.blumbit.eblumbit.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "permisos")
public class Permiso {

    @Id 
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 100, nullable = false)
    private String subject;

    @Column(length = 100, nullable = false)
    private String action;

    @ManyToMany(mappedBy = "permisos")
    private List<Rol> roles;

}
