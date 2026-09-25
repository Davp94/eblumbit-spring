package com.blumbit.eblumbit.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    private Integer id;

    @Column(length = 200, nullable = false)
    private String nombreCompleto;

    @Column (length = 30, nullable = false)
    private String nroIdentificacion;

    private LocalDate fechaNacimiento;

    @Column(length = 100, nullable = false)
    private String telefono;

    @Column(length = 150)
    private String correo;

    @Column(length = 150, nullable = false)
    private Boolean estado;
}
