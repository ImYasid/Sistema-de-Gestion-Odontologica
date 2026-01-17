package com.endonova.fichasservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * PRESUPUESTO - Según ficha PDF ENDONOVA
 * Costos del tratamiento
 */
@Entity
@Table(name = "presupuestos")
@Data
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    // N ACTOS (Número de sesiones/actos)
    private Integer numeroActos;

    // ACTIVIDAD (Descripción del tratamiento)
    @Column(length = 500)
    private String actividad;

    // COSTO DE TRATAMIENTO (COST UNITARIO)
    @Column(precision = 10, scale = 2)
    private BigDecimal costoTratamiento;

    // MATERIAL
    @Column(precision = 10, scale = 2)
    private BigDecimal costoMaterial;

    // TOTAL (calculado automáticamente)
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    // Método para calcular el total automáticamente
    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        BigDecimal suma = BigDecimal.ZERO;
        if (costoTratamiento != null) suma = suma.add(costoTratamiento);
        if (costoMaterial != null) suma = suma.add(costoMaterial);
        this.total = suma;
    }
}
