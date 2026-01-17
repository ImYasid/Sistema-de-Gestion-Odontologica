package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.FichaTecnica;
import com.endonova.fichasservice.repository.FichaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FichaTecnicaService {

    @Autowired
    private FichaTecnicaRepository repository;

    // Crear nueva ficha técnica
    public FichaTecnica crear(FichaTecnica ficha) {
        return repository.save(ficha);
    }

    // Listar todas las fichas
    public List<FichaTecnica> listarTodas() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<FichaTecnica> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar fichas de un paciente
    public List<FichaTecnica> buscarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    // Buscar fichas activas de un paciente
    public List<FichaTecnica> buscarActivasPorPaciente(Long pacienteId) {
        return repository.findByPacienteIdAndEstado(pacienteId, FichaTecnica.EstadoFicha.ACTIVA);
    }

    // Actualizar ficha
    public FichaTecnica actualizar(Long id, FichaTecnica fichaActualizada) {
        return repository.findById(id)
                .map(ficha -> {
                    ficha.setNumeroHistoriaClinica(fichaActualizada.getNumeroHistoriaClinica());
                    ficha.setObservaciones(fichaActualizada.getObservaciones());
                    ficha.setEstado(fichaActualizada.getEstado());
                    return repository.save(ficha);
                })
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada con ID: " + id));
    }

    // Cambiar estado de la ficha
    public FichaTecnica cambiarEstado(Long id, FichaTecnica.EstadoFicha nuevoEstado) {
        return repository.findById(id)
                .map(ficha -> {
                    ficha.setEstado(nuevoEstado);
                    return repository.save(ficha);
                })
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada con ID: " + id));
    }

    // Eliminar ficha
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
