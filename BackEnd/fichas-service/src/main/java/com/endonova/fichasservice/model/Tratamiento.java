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


public class Tratamiento {

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

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getAnestesiaUtilizada() {
		return anestesiaUtilizada;
	}

	public void setAnestesiaUtilizada(String anestesiaUtilizada) {
		this.anestesiaUtilizada = anestesiaUtilizada;
	}

	public String getAislamiento() {
		return aislamiento;
	}

	public void setAislamiento(String aislamiento) {
		this.aislamiento = aislamiento;
	}

	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}

	public Integer getNumeroConductos() {
		return numeroConductos;
	}

	public void setNumeroConductos(Integer numeroConductos) {
		this.numeroConductos = numeroConductos;
	}

	public String getLongitudTrabajo() {
		return longitudTrabajo;
	}

	public void setLongitudTrabajo(String longitudTrabajo) {
		this.longitudTrabajo = longitudTrabajo;
	}

	public String getInstrumentacion() {
		return instrumentacion;
	}

	public void setInstrumentacion(String instrumentacion) {
		this.instrumentacion = instrumentacion;
	}

	public String getIrrigacion() {
		return irrigacion;
	}

	public void setIrrigacion(String irrigacion) {
		this.irrigacion = irrigacion;
	}

	public String getMedicacionIntracanal() {
		return medicacionIntracanal;
	}

	public void setMedicacionIntracanal(String medicacionIntracanal) {
		this.medicacionIntracanal = medicacionIntracanal;
	}

	public String getObturacion() {
		return obturacion;
	}

	public void setObturacion(String obturacion) {
		this.obturacion = obturacion;
	}

	public String getRestauracionTemporal() {
		return restauracionTemporal;
	}

	public void setRestauracionTemporal(String restauracionTemporal) {
		this.restauracionTemporal = restauracionTemporal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

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
