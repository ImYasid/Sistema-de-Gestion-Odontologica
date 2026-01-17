package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.Tratamiento;
import com.endonova.fichasservice.service.TratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST PARA TRATAMIENTOS
 */
@RestController
@RequestMapping("/tratamientos")
@CrossOrigin(origins = "*")
public class TratamientoController {

    @Autowired
    private TratamientoService service;

    // POST /tratamientos - Crear tratamiento
    @PostMapping
    public ResponseEntity<Tratamiento> crear(@RequestBody Tratamiento tratamiento) {
        Tratamiento nuevoTratamiento = service.crear(tratamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTratamiento);
    }

    // GET /tratamientos - Listar todos
    @GetMapping
    public ResponseEntity<List<Tratamiento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /tratamientos/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tratamiento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /tratamientos/ficha/{fichaTecnicaId} - Tratamientos de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}")
    public ResponseEntity<List<Tratamiento>> buscarPorFicha(@PathVariable Long fichaTecnicaId) {
        return ResponseEntity.ok(service.buscarPorFicha(fichaTecnicaId));
    }

    // PUT /tratamientos/{id} - Actualizar tratamiento
    @PutMapping("/{id}")
    public ResponseEntity<Tratamiento> actualizar(@PathVariable Long id, @RequestBody Tratamiento tratamiento) {
        try {
            Tratamiento tratamientoActualizado = service.actualizar(id, tratamiento);
            return ResponseEntity.ok(tratamientoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /tratamientos/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
