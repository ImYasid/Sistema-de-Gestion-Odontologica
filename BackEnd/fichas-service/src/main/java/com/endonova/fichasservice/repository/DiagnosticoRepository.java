package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    
    // Buscar todos los diagnósticos de una ficha
    List<Diagnostico> findByFichaTecnicaId(Long fichaTecnicaId);
    
    // Buscar diagnósticos por número de diente
    List<Diagnostico> findByPiezaDental(String piezaDental);
    
    // Buscar diagnósticos de un diente en una ficha específica
    List<Diagnostico> findByFichaTecnicaIdAndPiezaDental(Long fichaTecnicaId, String piezaDental);
}
