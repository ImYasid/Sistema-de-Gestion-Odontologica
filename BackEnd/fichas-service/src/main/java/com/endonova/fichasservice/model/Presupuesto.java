package com.endonova.fichasservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * PRESUPUESTO - Corregido por el Arquitecto Yasid
 */
@Entity
@Table(name = "presupuestos")


public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    private Integer numeroActos;

    @Column(length = 500)
    private String actividad;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FichaTecnica getFichaTecnica() {
		return fichaTecnica;
	}

	public void setFichaTecnica(FichaTecnica fichaTecnica) {
		this.fichaTecnica = fichaTecnica;
	}

	public Integer getNumeroActos() {
		return numeroActos;
	}

	public void setNumeroActos(Integer numeroActos) {
		this.numeroActos = numeroActos;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public BigDecimal getCostoTratamiento() {
		return costoTratamiento;
	}

	public void setCostoTratamiento(BigDecimal costoTratamiento) {
		this.costoTratamiento = costoTratamiento;
	}

	public BigDecimal getCostoMaterial() {
		return costoMaterial;
	}

	public void setCostoMaterial(BigDecimal costoMaterial) {
		this.costoMaterial = costoMaterial;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public EstadoPresupuesto getEstado() {
		return estado;
	}

	public void setEstado(EstadoPresupuesto estado) {
		this.estado = estado;
	}

	@Column(precision = 10, scale = 2)
    private BigDecimal costoTratamiento;

    @Column(precision = 10, scale = 2)
    private BigDecimal costoMaterial;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    // --- LA PARTE NUEVA QUE FALTABA ---
    @Enumerated(EnumType.STRING)
    private EstadoPresupuesto estado;

    public enum EstadoPresupuesto {
        PENDIENTE,
        APROBADO,
        RECHAZADO,
        PAGADO
    }
    // ----------------------------------

    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        BigDecimal suma = BigDecimal.ZERO;
        if (costoTratamiento != null) suma = suma.add(costoTratamiento);
        if (costoMaterial != null) suma = suma.add(costoMaterial);
        this.total = suma;
    }
}