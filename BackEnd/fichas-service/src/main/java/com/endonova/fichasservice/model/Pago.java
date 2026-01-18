package com.endonova.fichasservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * REGISTRO DE PAGOS - Según ficha PDF ENDONOVA
 * Control de los pagos efectuados por el paciente
 */
@Entity
@Table(name = "pagos")

public class Pago {

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

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getNumeroRecibo() {
		return numeroRecibo;
	}

	public void setNumeroRecibo(String numeroRecibo) {
		this.numeroRecibo = numeroRecibo;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
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

    // ACTIVIDAD (Por qué concepto se paga)
    @Column(length = 300)
    private String actividad;

    // MONTO (VALOR)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    // SALDO (Saldo restante después del pago)
    @Column(precision = 10, scale = 2)
    private BigDecimal saldo;

    // MÉTODO DE PAGO
    @Column(length = 100)
    private String metodoPago; // Ej: "Efectivo", "Transferencia", "Tarjeta"

    // RECIBO
    @Column(length = 100)
    private String numeroRecibo;

    // FIRMA (del paciente)
    @Column(length = 200)
    private String firma;
}
