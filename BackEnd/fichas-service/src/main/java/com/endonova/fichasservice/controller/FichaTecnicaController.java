package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.FichaTecnica;
import com.endonova.fichasservice.service.FichaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST PARA FICHAS TÉCNICAS
 * Endpoints para gestionar las fichas de diagnóstico y tratamiento
 */
@RestController
@RequestMapping("/fichas")
@CrossOrigin(origins = "*")
public class FichaTecnicaController {

    @Autowired
    private FichaTecnicaService service;

    // POST /fichas - Crear nueva ficha técnica
    @PostMapping
    public ResponseEntity<FichaTecnica> crear(@RequestBody FichaTecnica ficha) {
        FichaTecnica nuevaFicha = service.crear(ficha);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFicha);
    }

    // GET /fichas - Listar todas las fichas
    @GetMapping
    public ResponseEntity<List<FichaTecnica>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /fichas/{id} - Buscar ficha por ID
    @GetMapping("/{id}")
    public ResponseEntity<FichaTecnica> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /fichas/paciente/{pacienteId} - Buscar fichas de un paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<FichaTecnica>> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.buscarPorPaciente(pacienteId));
    }

    // GET /fichas/paciente/{pacienteId}/activas - Fichas activas de un paciente
    @GetMapping("/paciente/{pacienteId}/activas")
    public ResponseEntity<List<FichaTecnica>> buscarActivasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.buscarActivasPorPaciente(pacienteId));
    }

    // PUT /fichas/{id} - Actualizar ficha
    @PutMapping("/{id}")
    public ResponseEntity<FichaTecnica> actualizar(@PathVariable Long id, @RequestBody FichaTecnica ficha) {
        try {
            FichaTecnica fichaActualizada = service.actualizar(id, ficha);
            return ResponseEntity.ok(fichaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH /fichas/{id}/estado - Cambiar estado de la ficha
    @PatchMapping("/{id}/estado")
    public ResponseEntity<FichaTecnica> cambiarEstado(@PathVariable Long id, 
                                                       @RequestParam String estado) {
        try {
            FichaTecnica.EstadoFicha nuevoEstado = FichaTecnica.EstadoFicha.valueOf(estado);
            FichaTecnica fichaActualizada = service.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(fichaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /fichas/{id} - Eliminar ficha
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
