package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.Anamnesis;
import com.endonova.fichasservice.service.AnamnesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR REST PARA ANAMNESIS (Historia Clínica)
 */
@RestController
@RequestMapping("/anamnesis")
@CrossOrigin(origins = "*")
public class AnamnesisController {

    @Autowired
    private AnamnesisService service;

    // POST /anamnesis - Crear anamnesis
    @PostMapping
    public ResponseEntity<Anamnesis> crear(@RequestBody Anamnesis anamnesis) {
        Anamnesis nuevaAnamnesis = service.crear(anamnesis);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAnamnesis);
    }

    // GET /anamnesis - Listar todas
    @GetMapping
    public ResponseEntity<List<Anamnesis>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /anamnesis/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Anamnesis> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /anamnesis/ficha/{fichaTecnicaId} - Anamnesis de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}")
    public ResponseEntity<Anamnesis> buscarPorFicha(@PathVariable Long fichaTecnicaId) {
        return service.buscarPorFicha(fichaTecnicaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /anamnesis/{id} - Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Anamnesis> actualizar(@PathVariable Long id, @RequestBody Anamnesis anamnesis) {
        try {
            Anamnesis anamnesisActualizada = service.actualizar(id, anamnesis);
            return ResponseEntity.ok(anamnesisActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /anamnesis/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
