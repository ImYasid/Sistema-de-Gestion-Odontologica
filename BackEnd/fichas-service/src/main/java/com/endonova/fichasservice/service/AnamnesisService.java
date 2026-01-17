package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.Anamnesis;
import com.endonova.fichasservice.repository.AnamnesisRepository;
import com.endonova.fichasservice.repository.FichaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnamnesisService {

    @Autowired
    private AnamnesisRepository repository;

    @Autowired
    private FichaTecnicaRepository fichaTecnicaRepository;

    // Crear anamnesis
    public Anamnesis crear(Anamnesis anamnesis) {
        // Verificar que la ficha técnica existe
        if (anamnesis.getFichaTecnica() != null && anamnesis.getFichaTecnica().getId() != null) {
            fichaTecnicaRepository.findById(anamnesis.getFichaTecnica().getId())
                    .orElseThrow(() -> new RuntimeException("Ficha técnica no encontrada"));
        }
        return repository.save(anamnesis);
    }

    // Listar todas
    public List<Anamnesis> listarTodas() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Anamnesis> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar anamnesis de una ficha
    public Optional<Anamnesis> buscarPorFicha(Long fichaTecnicaId) {
        return repository.findByFichaTecnicaId(fichaTecnicaId);
    }

    // Actualizar anamnesis
    public Anamnesis actualizar(Long id, Anamnesis anamnesisActualizada) {
        return repository.findById(id)
                .map(anamnesis -> {
                    anamnesis.setDrReferidor(anamnesisActualizada.getDrReferidor());
                    anamnesis.setMotivoConsulta(anamnesisActualizada.getMotivoConsulta());
                    anamnesis.setAntecedentesEnfermedadActual(anamnesisActualizada.getAntecedentesEnfermedadActual());
                    anamnesis.setEnfermedadSistematica(anamnesisActualizada.getEnfermedadSistematica());
                    anamnesis.setCualEnfermedad(anamnesisActualizada.getCualEnfermedad());
                    anamnesis.setAlergias(anamnesisActualizada.getAlergias());
                    anamnesis.setCualAlergia(anamnesisActualizada.getCualAlergia());
                    anamnesis.setTomaMedicamento(anamnesisActualizada.getTomaMedicamento());
                    anamnesis.setCualMedicamento(anamnesisActualizada.getCualMedicamento());
                    anamnesis.setEmbarazada(anamnesisActualizada.getEmbarazada());
                    return repository.save(anamnesis);
                })
                .orElseThrow(() -> new RuntimeException("Anamnesis no encontrada con ID: " + id));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
