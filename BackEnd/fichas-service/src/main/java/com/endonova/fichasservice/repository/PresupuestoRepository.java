package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    
    // Buscar presupuesto por ficha técnica
    Optional<Presupuesto> findByFichaTecnicaId(Long fichaTecnicaId);
    
    // Buscar presupuestos por estado
    List<Presupuesto> findByEstado(Presupuesto.EstadoPresupuesto estado);
}
