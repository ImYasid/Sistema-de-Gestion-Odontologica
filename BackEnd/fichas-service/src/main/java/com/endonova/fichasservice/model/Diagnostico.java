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
@Data
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
