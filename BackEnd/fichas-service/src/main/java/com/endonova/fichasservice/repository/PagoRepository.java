package com.endonova.fichasservice.repository;

import com.endonova.fichasservice.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    // Buscar todos los pagos de una ficha
    List<Pago> findByFichaTecnicaId(Long fichaTecnicaId);
    
    // Calcular el total pagado para una ficha (simplificado sin estado)
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.fichaTecnica.id = :fichaTecnicaId")
    BigDecimal calcularTotalPagado(Long fichaTecnicaId);
}
