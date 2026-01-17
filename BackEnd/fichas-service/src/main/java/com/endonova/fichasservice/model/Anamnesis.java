package com.endonova.fichasservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * ANAMNESIS - Según ficha PDF ENDONOVA
 * Historia Clínica del Paciente
 */
@Entity
@Table(name = "anamnesis")
@Data
public class Anamnesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id", nullable = false)
    private FichaTecnica fichaTecnica;

    // DR. REFERIDOR
    @Column(length = 200)
    private String drReferidor;

    // MOTIVO DE CONSULTA
    @Column(length = 1000, nullable = false)
    private String motivoConsulta;

    // ANTECEDENTES DE LA ENFERMEDAD ACTUAL
    @Column(length = 1000)
    private String antecedentesEnfermedadActual;

    // ¿SUFRE ALGUNA ENFERMEDAD SISTEMÁTICA?
    private Boolean enfermedadSistematica;
    
    @Column(length = 500)
    private String cualEnfermedad;

    // ¿TIENE ALGUNA ALERGIA?
    private Boolean alergias;
    
    @Column(length = 500)
    private String cualAlergia;

    // ¿ESTÁ TOMANDO ALGÚN MEDICAMENTO?
    private Boolean tomaMedicamento;
    
    @Column(length = 500)
    private String cualMedicamento;

    // ¿ESTÁ EMBARAZADA?
    private Boolean embarazada;
}
