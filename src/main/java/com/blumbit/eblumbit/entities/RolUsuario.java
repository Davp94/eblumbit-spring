package com.blumbit.eblumbit.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name="role_user")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class RolUsuario {

    @Id 
    private Integer id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")  
    private Rol rol;

    @ManyToOne 
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}
