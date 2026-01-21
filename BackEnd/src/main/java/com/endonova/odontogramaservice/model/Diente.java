package com.endonova.odontogramaservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // <--- Nuevo
import lombok.Data;
import lombok.NoArgsConstructor;  // <--- Nuevo

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Table(name = "dientes_paciente")
public class Diente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId; 

    @Column(nullable = false)
    private Integer numeroDiente; 

    private String estado; 

    private String color; 
    
    private String observacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Integer getNumeroDiente() {
		return numeroDiente;
	}

	public void setNumeroDiente(Integer numeroDiente) {
		this.numeroDiente = numeroDiente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

    
}