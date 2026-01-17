package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.Diagnostico;
import com.endonova.fichasservice.service.DiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST PARA DIAGNÓSTICOS
 */
@RestController
@RequestMapping("/diagnosticos")
@CrossOrigin(origins = "*")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService service;

    // POST /diagnosticos - Crear diagnóstico
    @PostMapping
    public ResponseEntity<Diagnostico> crear(@RequestBody Diagnostico diagnostico) {
        Diagnostico nuevoDiagnostico = service.crear(diagnostico);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiagnostico);
    }

    // GET /diagnosticos - Listar todos
    @GetMapping
    public ResponseEntity<List<Diagnostico>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /diagnosticos/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Diagnostico> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /diagnosticos/ficha/{fichaTecnicaId} - Diagnósticos de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}")
    public ResponseEntity<List<Diagnostico>> buscarPorFicha(@PathVariable Long fichaTecnicaId) {
        return ResponseEntity.ok(service.buscarPorFicha(fichaTecnicaId));
    }

    // GET /diagnosticos/pieza/{piezaDental} - Diagnósticos por pieza dental
    @GetMapping("/pieza/{piezaDental}")
    public ResponseEntity<List<Diagnostico>> buscarPorPieza(@PathVariable String piezaDental) {
        return ResponseEntity.ok(service.buscarPorDiente(piezaDental));
    }

    // PUT /diagnosticos/{id} - Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Diagnostico> actualizar(@PathVariable Long id, @RequestBody Diagnostico diagnostico) {
        try {
            Diagnostico diagnosticoActualizado = service.actualizar(id, diagnostico);
            return ResponseEntity.ok(diagnosticoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /diagnosticos/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
