package com.endonova.fichasservice.service;

import com.endonova.fichasservice.model.Tratamiento;
import com.endonova.fichasservice.repository.TratamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TratamientoService {

    @Autowired
    private TratamientoRepository repository;

    // Crear tratamiento
    public Tratamiento crear(Tratamiento tratamiento) {
        return repository.save(tratamiento);
    }

    // Listar todos
    public List<Tratamiento> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public Optional<Tratamiento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Buscar tratamientos de una ficha (ordenados por fecha)
    public List<Tratamiento> buscarPorFicha(Long fichaTecnicaId) {
        return repository.findByFichaTecnicaIdOrderByFechaAsc(fichaTecnicaId);
    }

    // Actualizar tratamiento
    public Tratamiento actualizar(Long id, Tratamiento tratamientoActualizado) {
        return repository.findById(id)
                .map(tratamiento -> {
                    tratamiento.setFecha(tratamientoActualizado.getFecha());
                    tratamiento.setAnestesiaUtilizada(tratamientoActualizado.getAnestesiaUtilizada());
                    tratamiento.setAislamiento(tratamientoActualizado.getAislamiento());
                    tratamiento.setAcceso(tratamientoActualizado.getAcceso());
                    tratamiento.setNumeroConductos(tratamientoActualizado.getNumeroConductos());
                    tratamiento.setLongitudTrabajo(tratamientoActualizado.getLongitudTrabajo());
                    tratamiento.setInstrumentacion(tratamientoActualizado.getInstrumentacion());
                    tratamiento.setIrrigacion(tratamientoActualizado.getIrrigacion());
                    tratamiento.setMedicacionIntracanal(tratamientoActualizado.getMedicacionIntracanal());
                    tratamiento.setObturacion(tratamientoActualizado.getObturacion());
                    tratamiento.setRestauracionTemporal(tratamientoActualizado.getRestauracionTemporal());
                    tratamiento.setObservaciones(tratamientoActualizado.getObservaciones());
                    return repository.save(tratamiento);
                })
                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado con ID: " + id));
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
