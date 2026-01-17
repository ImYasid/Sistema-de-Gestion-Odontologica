package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {
    
    // Buscar todos los tratamientos de una ficha
    List<Tratamiento> findByFichaTecnicaId(Long fichaTecnicaId);
    
    // Buscar tratamientos de una ficha ordenados por fecha
    List<Tratamiento> findByFichaTecnicaIdOrderByFechaAsc(Long fichaTecnicaId);
}
