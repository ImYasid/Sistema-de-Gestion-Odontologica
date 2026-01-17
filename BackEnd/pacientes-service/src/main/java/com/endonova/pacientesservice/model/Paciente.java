package com.endonova.pacientesservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cedula;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;
    private String telefono;
    private String direccion;
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}
