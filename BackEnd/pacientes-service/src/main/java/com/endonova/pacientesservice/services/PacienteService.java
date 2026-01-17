package com.endonova.pacientesservice.services;

import com.endonova.pacientesservice.model.Paciente;
import com.endonova.pacientesservice.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository repository;

    //Listar todos los pacientes
    public List<Paciente> listarTodos(){
        return repository.findAll();
    }

    //Guardar paciente
    public Paciente guardarPaciente(Paciente paciente){
        return repository.save(paciente);
    }

    //Buscar por ID
    public Optional<Paciente> buscarPorId(Long id){
        return repository.findById(id);
    }

    //Buscar por cedula
    public Optional<Paciente> buscarPorCedula(String cedula){
        return repository.findByCedula(cedula);
    }

    // Eliminar paciente
    public void eliminarPaciente(Long id){
        repository.deleteById(id);
    }
}
