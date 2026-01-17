package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.Presupuesto;
import com.endonova.fichasservice.repository.PresupuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PresupuestoService {

    @Autowired
    private PresupuestoRepository repository;

    // Crear presupuesto
    public Presupuesto crear(Presupuesto presupuesto) {
        return repository.save(presupuesto);
    }

    // Listar todos
    public List<Presupuesto> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Presupuesto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar presupuesto de una ficha
    public Optional<Presupuesto> buscarPorFicha(Long fichaTecnicaId) {
        return repository.findByFichaTecnicaId(fichaTecnicaId);
    }

    // Actualizar presupuesto
    public Presupuesto actualizar(Long id, Presupuesto presupuestoActualizado) {
        return repository.findById(id)
                .map(presupuesto -> {
                    presupuesto.setNumeroActos(presupuestoActualizado.getNumeroActos());
                    presupuesto.setActividad(presupuestoActualizado.getActividad());
                    presupuesto.setCostoTratamiento(presupuestoActualizado.getCostoTratamiento());
                    presupuesto.setCostoMaterial(presupuestoActualizado.getCostoMaterial());
                    return repository.save(presupuesto);
                })
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con ID: " + id));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
