package com.endonova.fichasservice.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * EXAMEN CLÍNICO + DIAGNÓSTICO - Según ficha PDF ENDONOVA
 * Incluye: Examen Clínico, Pruebas de Vitalidad, Examen Radiográfico y Diagnóstico
 */
@Entity
@Table(name = "diagnosticos")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ficha_tecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    // PIEZA DENTAL
    @Column(length = 10, nullable = false)
    private String piezaDental;

    // ========== CAUSAS ==========
    @Column(length = 200)
    private String causas; // Caries / Reabsorciones / Tratamiento Anterior / Traumatismo / Finalidad Protética / Endoperiodontal / Otras

    // ========== DOLOR ==========
    @Column(length = 50)
    private String dolorNaturaleza; // No hay dolor / Leve / Moderado / Intenso

    @Column(length = 50)
    private String dolorCalidad; // Sordo / Agudo / Pulsátil / Continuo / Irradiada

    @Column(length = 50)
    private String dolorLocalizacion; // Localizado / Difuso / Referido

    @Column(length = 200)
    private String dolorIrradiadoA; // Si es "Irradiado", especificar dónde (ej: "región temporal", "mandíbula")

    @Column(length = 50)
    private String dolorDuracion; // Segundos / Pagas / Minutos / Horas / Persistente

    @Column(length = 200)
    private String dolorIniciadoPor; // Frío / Masticación / Calor / Dulces Ácidos / Percusión / Al acostarse / Espontáneo / No deja dormir

    // ========== EXAMEN CLÍNICO ==========
    @Column(length = 500)
    private String inspeccionVisual;

    @Column(length = 100)
    private String palpacion; // Ej: "Normal", "Dolor", "Inflamación"

    @Column(length = 100)
    private String percusion; // Ej: "Positivo", "Negativo"

    @Column(length = 100)
    private String movilidad; // Ej: "Grado 0", "Grado I", "Grado II", "Grado III"

    @Column(length = 100)
    private String cambioColoracion; // Ej: "Sin cambios", "Oscurecimiento"

    // ========== PRUEBAS DE VITALIDAD PULPAR ==========
    @Column(length = 100)
    private String pruebaTermicaFrio; // Ej: "+", "-", "Normal", "Dolor prolongado"

    @Column(length = 100)
    private String pruebaTermicaCalor; // Ej: "+", "-", "Normal", "Dolor prolongado"

    @Column(length = 100)
    private String pruebaElectrica; // Ej: "Positiva", "Negativa", "Respuesta tardía"

    // ========== ZONA PERIAPICAL ==========
    @Column(length = 50)
    private String zonaPeriapical; // Normal / Tumefacción / Adenopatías / Flemón

    private Boolean dolorPalpacion;

    private Boolean fistula;

    private Boolean flemon;

    // ========== EXAMEN PERIODONTAL ==========
    @Column(length = 50)
    private String profundidadBolsa; // En mm, puede ser múltiples medidas

    @Column(length = 20)
    private String movilidadGrado; // 0, 1, 2, 3

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

	public String getPiezaDental() {
		return piezaDental;
	}

	public void setPiezaDental(String piezaDental) {
		this.piezaDental = piezaDental;
	}

	public String getCausas() {
		return causas;
	}

	public void setCausas(String causas) {
		this.causas = causas;
	}

	public String getDolorNaturaleza() {
		return dolorNaturaleza;
	}

	public void setDolorNaturaleza(String dolorNaturaleza) {
		this.dolorNaturaleza = dolorNaturaleza;
	}

	public String getDolorCalidad() {
		return dolorCalidad;
	}

	public void setDolorCalidad(String dolorCalidad) {
		this.dolorCalidad = dolorCalidad;
	}

	public String getDolorLocalizacion() {
		return dolorLocalizacion;
	}

	public void setDolorLocalizacion(String dolorLocalizacion) {
		this.dolorLocalizacion = dolorLocalizacion;
	}

	public String getDolorIrradiadoA() {
		return dolorIrradiadoA;
	}

	public void setDolorIrradiadoA(String dolorIrradiadoA) {
		this.dolorIrradiadoA = dolorIrradiadoA;
	}

	public String getDolorDuracion() {
		return dolorDuracion;
	}

	public void setDolorDuracion(String dolorDuracion) {
		this.dolorDuracion = dolorDuracion;
	}

	public String getDolorIniciadoPor() {
		return dolorIniciadoPor;
	}

	public void setDolorIniciadoPor(String dolorIniciadoPor) {
		this.dolorIniciadoPor = dolorIniciadoPor;
	}

	public String getInspeccionVisual() {
		return inspeccionVisual;
	}

	public void setInspeccionVisual(String inspeccionVisual) {
		this.inspeccionVisual = inspeccionVisual;
	}

	public String getPalpacion() {
		return palpacion;
	}

	public void setPalpacion(String palpacion) {
		this.palpacion = palpacion;
	}

	public String getPercusion() {
		return percusion;
	}

	public void setPercusion(String percusion) {
		this.percusion = percusion;
	}

	public String getMovilidad() {
		return movilidad;
	}

	public void setMovilidad(String movilidad) {
		this.movilidad = movilidad;
	}

	public String getCambioColoracion() {
		return cambioColoracion;
	}

	public void setCambioColoracion(String cambioColoracion) {
		this.cambioColoracion = cambioColoracion;
	}

	public String getPruebaTermicaFrio() {
		return pruebaTermicaFrio;
	}

	public void setPruebaTermicaFrio(String pruebaTermicaFrio) {
		this.pruebaTermicaFrio = pruebaTermicaFrio;
	}

	public String getPruebaTermicaCalor() {
		return pruebaTermicaCalor;
	}

	public void setPruebaTermicaCalor(String pruebaTermicaCalor) {
		this.pruebaTermicaCalor = pruebaTermicaCalor;
	}

	public String getPruebaElectrica() {
		return pruebaElectrica;
	}

	public void setPruebaElectrica(String pruebaElectrica) {
		this.pruebaElectrica = pruebaElectrica;
	}

	public String getZonaPeriapical() {
		return zonaPeriapical;
	}

	public void setZonaPeriapical(String zonaPeriapical) {
		this.zonaPeriapical = zonaPeriapical;
	}

	public Boolean getDolorPalpacion() {
		return dolorPalpacion;
	}

	public void setDolorPalpacion(Boolean dolorPalpacion) {
		this.dolorPalpacion = dolorPalpacion;
	}

	public Boolean getFistula() {
		return fistula;
	}

	public void setFistula(Boolean fistula) {
		this.fistula = fistula;
	}

	public Boolean getFlemon() {
		return flemon;
	}

	public void setFlemon(Boolean flemon) {
		this.flemon = flemon;
	}

	public String getProfundidadBolsa() {
		return profundidadBolsa;
	}

	public void setProfundidadBolsa(String profundidadBolsa) {
		this.profundidadBolsa = profundidadBolsa;
	}

	public String getMovilidadGrado() {
		return movilidadGrado;
	}

	public void setMovilidadGrado(String movilidadGrado) {
		this.movilidadGrado = movilidadGrado;
	}

	public Boolean getSupuracion() {
		return supuracion;
	}

	public void setSupuracion(Boolean supuracion) {
		this.supuracion = supuracion;
	}

	public String getCamaraRadiografica() {
		return camaraRadiografica;
	}

	public void setCamaraRadiografica(String camaraRadiografica) {
		this.camaraRadiografica = camaraRadiografica;
	}

	public String getConductosRadiografia() {
		return conductosRadiografia;
	}

	public void setConductosRadiografia(String conductosRadiografia) {
		this.conductosRadiografia = conductosRadiografia;
	}

	public String getHallazgosRadiograficos() {
		return hallazgosRadiograficos;
	}

	public void setHallazgosRadiograficos(String hallazgosRadiograficos) {
		this.hallazgosRadiograficos = hallazgosRadiograficos;
	}

	public String getCausasFracasoTratamientoAnterior() {
		return causasFracasoTratamientoAnterior;
	}

	public void setCausasFracasoTratamientoAnterior(String causasFracasoTratamientoAnterior) {
		this.causasFracasoTratamientoAnterior = causasFracasoTratamientoAnterior;
	}

	public String getDiagnosticoPulpar() {
		return diagnosticoPulpar;
	}

	public void setDiagnosticoPulpar(String diagnosticoPulpar) {
		this.diagnosticoPulpar = diagnosticoPulpar;
	}

	public String getDiagnosticoPeriapical() {
		return diagnosticoPeriapical;
	}

	public void setDiagnosticoPeriapical(String diagnosticoPeriapical) {
		this.diagnosticoPeriapical = diagnosticoPeriapical;
	}

	public String getPlanTratamiento() {
		return planTratamiento;
	}

	public void setPlanTratamiento(String planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	private Boolean supuracion;

    // ========== EVALUACIÓN RADIOGRÁFICA ==========
    @Column(length = 200)
    private String camaraRadiografica; // Normal / Estrecha / Nódulos / Reabsorción interna

    @Column(length = 200)
    private String conductosRadiografia; // calcificado / Amplia / Reabsorción externa

    // ========== EXAMEN RADIOGRÁFICO (Hallazgos generales) ==========
    @Column(length = 1000)
    private String hallazgosRadiograficos;

    // ========== CAUSAS DEL FRACASO DEL TRATAMIENTO ANTERIOR ==========
    @Column(length = 500)
    private String causasFracasoTratamientoAnterior; // Filtración Coronaria / Escalón / Mantiene lesión periodontal / Inst. fracturado / Trat. Incompleto / Perforación / Trat. Subobturado / Finalidad Protética / Trat Sobreobturado

    // ========== DIAGNÓSTICO ==========
    @Column(length = 200)
    private String diagnosticoPulpar; // Texto libre según PDF

    @Column(length = 200)
    private String diagnosticoPeriapical; // Texto libre según PDF

    // ========== PLAN DE TRATAMIENTO ==========
    @Column(length = 1000)
    private String planTratamiento;
}
