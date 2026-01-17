package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.FichaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {
    
    // Buscar todas las fichas de un paciente específico
    List<FichaTecnica> findByPacienteId(Long pacienteId);
    
    // Buscar fichas por estado
    List<FichaTecnica> findByEstado(FichaTecnica.EstadoFicha estado);
    
    // Buscar fichas activas de un paciente
    List<FichaTecnica> findByPacienteIdAndEstado(Long pacienteId, FichaTecnica.EstadoFicha estado);
}
