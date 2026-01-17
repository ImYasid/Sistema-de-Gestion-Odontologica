package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {
    
    // Buscar anamnesis por ficha técnica
    Optional<Anamnesis> findByFichaTecnicaId(Long fichaTecnicaId);
}
