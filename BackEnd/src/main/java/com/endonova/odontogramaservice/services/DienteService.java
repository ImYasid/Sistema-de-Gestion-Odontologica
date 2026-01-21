package com.endonova.odontogramaservice.services;

import com.endonova.odontogramaservice.model.Diente;
import com.endonova.odontogramaservice.repository.DienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DienteService {

    @Autowired
    private DienteRepository repository;

    // Obtener toda la boca del paciente
    public List<Diente> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    // Guardar o Actualizar un diente
    public Diente guardar(Diente dienteNuevo) {
        // Buscamos si este paciente ya tiene registrado ese diente
        Optional<Diente> dienteExistente = repository.findByPacienteIdAndNumeroDiente(
            dienteNuevo.getPacienteId(), 
            dienteNuevo.getNumeroDiente()
        );

        if (dienteExistente.isPresent()) {
            // Si ya existe, actualizamos sus datos (ID se mantiene)
            Diente d = dienteExistente.get();
            d.setEstado(dienteNuevo.getEstado());
            d.setColor(dienteNuevo.getColor());
            d.setObservacion(dienteNuevo.getObservacion());
            return repository.save(d);
        } else {
            // Si es nuevo, lo guardamos tal cual
            return repository.save(dienteNuevo);
        }
    }
}