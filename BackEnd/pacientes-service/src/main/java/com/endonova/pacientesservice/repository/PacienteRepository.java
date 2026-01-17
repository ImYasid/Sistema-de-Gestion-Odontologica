package com.endonova.pacientesservice.repository;

import com.endonova.pacientesservice.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Método extra para buscar por cedula
    Optional<Paciente> findByCedula(String cedula);
}