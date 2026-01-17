package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.Pago;
import com.endonova.fichasservice.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository repository;

    // Crear pago
    public Pago crear(Pago pago) {
        return repository.save(pago);
    }

    // Listar todos
    public List<Pago> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Pago> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar pagos de una ficha
    public List<Pago> buscarPorFicha(Long fichaTecnicaId) {
        return repository.findByFichaTecnicaId(fichaTecnicaId);
    }

    // Calcular total pagado
    public BigDecimal calcularTotalPagado(Long fichaTecnicaId) {
        return repository.calcularTotalPagado(fichaTecnicaId);
    }

    // Actualizar pago
    public Pago actualizar(Long id, Pago pagoActualizado) {
        return repository.findById(id)
                .map(pago -> {
                    pago.setFecha(pagoActualizado.getFecha());
                    pago.setActividad(pagoActualizado.getActividad());
                    pago.setMonto(pagoActualizado.getMonto());
                    pago.setSaldo(pagoActualizado.getSaldo());
                    pago.setMetodoPago(pagoActualizado.getMetodoPago());
                    pago.setNumeroRecibo(pagoActualizado.getNumeroRecibo());
                    pago.setFirma(pagoActualizado.getFirma());
                    return repository.save(pago);
                })
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
