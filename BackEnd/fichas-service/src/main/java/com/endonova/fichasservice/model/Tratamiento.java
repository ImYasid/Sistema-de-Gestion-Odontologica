package com.endonova.fichasservice.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * TRATAMIENTO ENDODÓNTICO - Según ficha PDF ENDONOVA
 * Registro de procedimientos realizados en cada sesión
 */
@Entity
@Table(name = "tratamientos")
@Data
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ficha_tecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    // FECHA
    @Column(nullable = false)
    private LocalDate fecha;

    // ANESTESIA UTILIZADA
    @Column(length = 200)
    private String anestesiaUtilizada;

    // AISLAMIENTO
    @Column(length = 200)
    private String aislamiento;

    // ACCESO
    @Column(length = 500)
    private String acceso;

    // NÚMERO DE CONDUCTOS
    private Integer numeroConductos;

    // LONGITUD DE TRABAJO
    @Column(length = 200)
    private String longitudTrabajo;

    // INSTRUMENTACIÓN
    @Column(length = 500)
    private String instrumentacion;

    // IRRIGACIÓN
    @Column(length = 500)
    private String irrigacion;

    // MEDICACIÓN INTRACANAL
    @Column(length = 500)
    private String medicacionIntracanal;

    // OBTURACIÓN
    @Column(length = 500)
    private String obturacion;

    // RESTAURACIÓN TEMPORAL
    @Column(length = 500)
    private String restauracionTemporal;

    // OBSERVACIONES
    @Column(length = 1000)
    private String observaciones;
}
