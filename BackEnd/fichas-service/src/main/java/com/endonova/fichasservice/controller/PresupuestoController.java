package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.Presupuesto;
import com.endonova.fichasservice.service.PresupuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST PARA PRESUPUESTOS
 */
@RestController
@RequestMapping("/presupuestos")
@CrossOrigin(origins = "*")
public class PresupuestoController {

    @Autowired
    private PresupuestoService service;

    // POST /presupuestos - Crear presupuesto
    @PostMapping
    public ResponseEntity<Presupuesto> crear(@RequestBody Presupuesto presupuesto) {
        Presupuesto nuevoPresupuesto = service.crear(presupuesto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPresupuesto);
    }

    // GET /presupuestos - Listar todos
    @GetMapping
    public ResponseEntity<List<Presupuesto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /presupuestos/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Presupuesto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /presupuestos/ficha/{fichaTecnicaId} - Presupuesto de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}")
    public ResponseEntity<Presupuesto> buscarPorFicha(@PathVariable Long fichaTecnicaId) {
        return service.buscarPorFicha(fichaTecnicaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /presupuestos/{id} - Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Presupuesto> actualizar(@PathVariable Long id, @RequestBody Presupuesto presupuesto) {
        try {
            Presupuesto presupuestoActualizado = service.actualizar(id, presupuesto);
            return ResponseEntity.ok(presupuestoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /presupuestos/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
