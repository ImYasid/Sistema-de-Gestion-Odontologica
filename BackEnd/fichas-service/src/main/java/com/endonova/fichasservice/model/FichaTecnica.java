package com.endonova.fichasservice.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * FICHA TÉCNICA DE DIAGNÓSTICO Y TRATAMIENTO ENDODÓNTICO
 * Esta es la entidad principal que agrupa toda la información
 * de un tratamiento endodóntico para un paciente.
 */

@Entity
@Table(name = "fichas_tecnicas")
public class FichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N HISTORIA CLINICA (del PDF - encabezado)
    @Column(name = "numero_historia_clinica", unique = true, length = 50)
    private String numeroHistoriaClinica;

    // RELACIÓN CON EL PACIENTE (del otro microservicio)
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId; // ID del paciente en el servicio de pacientes

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(length = 500)
    private String observaciones;

    // ESTADO DE LA FICHA
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoFicha estado = EstadoFicha.ACTIVA;

    // RELACIONES CON OTRAS ENTIDADES
    @OneToOne(mappedBy = "fichaTecnica", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("fichaTecnica")
    private Anamnesis anamnesis;

    @OneToMany(mappedBy = "fichaTecnica", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("fichaTecnica")
    private List<Diagnostico> diagnosticos;

    @OneToMany(mappedBy = "fichaTecnica", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("fichaTecnica")
    private List<Tratamiento> tratamientos;

    @OneToOne(mappedBy = "fichaTecnica", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("fichaTecnica")
    private Presupuesto presupuesto;

    @OneToMany(mappedBy = "fichaTecnica", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("fichaTecnica")
    private List<Pago> pagos;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}

	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}

	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstadoFicha getEstado() {
		return estado;
	}

	public void setEstado(EstadoFicha estado) {
		this.estado = estado;
	}

	public Anamnesis getAnamnesis() {
		return anamnesis;
	}

	public void setAnamnesis(Anamnesis anamnesis) {
		this.anamnesis = anamnesis;
	}

	public List<Diagnostico> getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(List<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public List<Tratamiento> getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(List<Tratamiento> tratamientos) {
		this.tratamientos = tratamientos;
	}

	public Presupuesto getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	@PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoFicha {
        ACTIVA,
        COMPLETADA,
        CANCELADA,
        EN_PAUSA
    }
}
