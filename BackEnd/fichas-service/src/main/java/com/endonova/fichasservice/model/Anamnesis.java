package com.endonova.fichasservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "anamnesis")
public class Anamnesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la Ficha Técnica (Una ficha tiene una anamnesis)
    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id")
    private FichaTecnica fichaTecnica;

    // --- LOS CAMPOS QUE FALTABAN Y CAUSABAN EL ERROR ---

    private String drReferidor;

    @Column(length = 1000)
    private String motivoConsulta;

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

	public String getDrReferidor() {
		return drReferidor;
	}

	public void setDrReferidor(String drReferidor) {
		this.drReferidor = drReferidor;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public String getAntecedentesEnfermedadActual() {
		return antecedentesEnfermedadActual;
	}

	public void setAntecedentesEnfermedadActual(String antecedentesEnfermedadActual) {
		this.antecedentesEnfermedadActual = antecedentesEnfermedadActual;
	}

	public Boolean getEnfermedadSistematica() {
		return enfermedadSistematica;
	}

	public void setEnfermedadSistematica(Boolean enfermedadSistematica) {
		this.enfermedadSistematica = enfermedadSistematica;
	}

	public String getCualEnfermedad() {
		return cualEnfermedad;
	}

	public void setCualEnfermedad(String cualEnfermedad) {
		this.cualEnfermedad = cualEnfermedad;
	}

	public Boolean getAlergias() {
		return alergias;
	}

	public void setAlergias(Boolean alergias) {
		this.alergias = alergias;
	}

	public String getCualAlergia() {
		return cualAlergia;
	}

	public void setCualAlergia(String cualAlergia) {
		this.cualAlergia = cualAlergia;
	}

	public Boolean getTomaMedicamento() {
		return tomaMedicamento;
	}

	public void setTomaMedicamento(Boolean tomaMedicamento) {
		this.tomaMedicamento = tomaMedicamento;
	}

	public String getCualMedicamento() {
		return cualMedicamento;
	}

	public void setCualMedicamento(String cualMedicamento) {
		this.cualMedicamento = cualMedicamento;
	}

	public Boolean getEmbarazada() {
		return embarazada;
	}

	public void setEmbarazada(Boolean embarazada) {
		this.embarazada = embarazada;
	}

	@Column(length = 1000)
    private String antecedentesEnfermedadActual;

    // ¿Sufre enfermedad sistemática? (Sí/No)
    private Boolean enfermedadSistematica;
    private String cualEnfermedad;

    // ¿Tiene alergias?
    private Boolean alergias;
    private String cualAlergia;

    // ¿Toma medicamentos?
    private Boolean tomaMedicamento;
    private String cualMedicamento;

    // ¿Está embarazada?
    private Boolean embarazada;
}