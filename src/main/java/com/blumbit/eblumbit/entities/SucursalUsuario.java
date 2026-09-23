package com.blumbit.eblumbit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class SucursalUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")  
    private Rol rol;

    @ManyToOne 
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;


    @ManyToOne 
    @JoinColumn(name = "sucursal_id", referencedColumnName = "id")
    private Sucursal sucursal;
}
