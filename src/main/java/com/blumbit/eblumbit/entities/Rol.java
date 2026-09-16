package com.blumbit.eblumbit.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name = "roles")
public class Rol {
    
    @Id 
    private Integer id;

    @Column(length = 100, nullable = false) 
    private String nombre;

    @Column(length = 250) 
    private String descripcion;

    @ManyToMany
    @JoinTable(
        name = "permiso_role",
        joinColumns = @JoinColumn(name="rol_id"),
        inverseJoinColumns = @JoinColumn(name="permiso_id")
    )  
    private List<Permiso> permisos;
}
