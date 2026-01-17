package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.Diagnostico;
import com.endonova.fichasservice.repository.DiagnosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiagnosticoService {

    @Autowired
    private DiagnosticoRepository repository;

    // Crear diagnóstico
    public Diagnostico crear(Diagnostico diagnostico) {
        return repository.save(diagnostico);
    }

    // Listar todos
    public List<Diagnostico> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Diagnostico> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar diagnósticos de una ficha
    public List<Diagnostico> buscarPorFicha(Long fichaTecnicaId) {
        return repository.findByFichaTecnicaId(fichaTecnicaId);
    }

    // Buscar diagnósticos de un diente
    public List<Diagnostico> buscarPorDiente(String piezaDental) {
        return repository.findByPiezaDental(piezaDental);
    }

    // Actualizar diagnóstico
    public Diagnostico actualizar(Long id, Diagnostico diagnosticoActualizado) {
        return repository.findById(id)
                .map(diagnostico -> {
                    diagnostico.setPiezaDental(diagnosticoActualizado.getPiezaDental());
                    diagnostico.setCausas(diagnosticoActualizado.getCausas());
                    // Sección DOLOR
                    diagnostico.setDolorNaturaleza(diagnosticoActualizado.getDolorNaturaleza());
                    diagnostico.setDolorCalidad(diagnosticoActualizado.getDolorCalidad());
                    diagnostico.setDolorLocalizacion(diagnosticoActualizado.getDolorLocalizacion());
                    diagnostico.setDolorIrradiadoA(diagnosticoActualizado.getDolorIrradiadoA());
                    diagnostico.setDolorDuracion(diagnosticoActualizado.getDolorDuracion());
                    diagnostico.setDolorIniciadoPor(diagnosticoActualizado.getDolorIniciadoPor());
                    // Examen Clínico
                    diagnostico.setInspeccionVisual(diagnosticoActualizado.getInspeccionVisual());
                    diagnostico.setPalpacion(diagnosticoActualizado.getPalpacion());
                    diagnostico.setPercusion(diagnosticoActualizado.getPercusion());
                    diagnostico.setMovilidad(diagnosticoActualizado.getMovilidad());
                    diagnostico.setCambioColoracion(diagnosticoActualizado.getCambioColoracion());
                    // Pruebas de Vitalidad
                    diagnostico.setPruebaTermicaFrio(diagnosticoActualizado.getPruebaTermicaFrio());
                    diagnostico.setPruebaTermicaCalor(diagnosticoActualizado.getPruebaTermicaCalor());
                    diagnostico.setPruebaElectrica(diagnosticoActualizado.getPruebaElectrica());
                    // Zona Periapical
                    diagnostico.setZonaPeriapical(diagnosticoActualizado.getZonaPeriapical());
                    diagnostico.setDolorPalpacion(diagnosticoActualizado.getDolorPalpacion());
                    diagnostico.setFistula(diagnosticoActualizado.getFistula());
                    diagnostico.setFlemon(diagnosticoActualizado.getFlemon());
                    // Examen Periodontal
                    diagnostico.setProfundidadBolsa(diagnosticoActualizado.getProfundidadBolsa());
                    diagnostico.setMovilidadGrado(diagnosticoActualizado.getMovilidadGrado());
                    diagnostico.setSupuracion(diagnosticoActualizado.getSupuracion());
                    // Evaluación Radiográfica
                    diagnostico.setCamaraRadiografica(diagnosticoActualizado.getCamaraRadiografica());
                    diagnostico.setConductosRadiografia(diagnosticoActualizado.getConductosRadiografia());
                    diagnostico.setHallazgosRadiograficos(diagnosticoActualizado.getHallazgosRadiograficos());
                    diagnostico.setCausasFracasoTratamientoAnterior(diagnosticoActualizado.getCausasFracasoTratamientoAnterior());
                    // Diagnóstico y Plan
                    diagnostico.setDiagnosticoPulpar(diagnosticoActualizado.getDiagnosticoPulpar());
                    diagnostico.setDiagnosticoPeriapical(diagnosticoActualizado.getDiagnosticoPeriapical());
                    diagnostico.setPlanTratamiento(diagnosticoActualizado.getPlanTratamiento());
                    return repository.save(diagnostico);
                })
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + id));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
