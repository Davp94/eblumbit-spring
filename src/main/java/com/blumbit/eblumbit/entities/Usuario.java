package com.blumbit.eblumbit.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity 
@Builder 
@AllArgsConstructor 
@Table(name = "users")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(nullable = false) 
    private boolean estado; 

    @Column(unique = true, nullable = false, name = "name")
    private String username;

    @Column(nullable = false, name = "password", length = 250)
    private String password;

    @Column(unique = true, nullable = false, name = "email", length = 200)
    private String email;

    @OneToMany(mappedBy = "usuario")
    private List<RolUsuario> rolesUsuario;

    public Usuario(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Usuario() {}

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    

    

}
