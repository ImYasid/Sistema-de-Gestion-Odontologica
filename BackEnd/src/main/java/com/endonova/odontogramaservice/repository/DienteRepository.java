package com.endonova.odontogramaservice.repository;

import com.endonova.odontogramaservice.model.Diente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DienteRepository extends JpaRepository<Diente, Long> {
    
    // Buscar todos los dientes de un paciente para pintarlos
    List<Diente> findByPacienteId(Long pacienteId);
    
    // Buscar un diente específico para ver si ya existe antes de guardarlo
    Optional<Diente> findByPacienteIdAndNumeroDiente(Long pacienteId, Integer numeroDiente);
}