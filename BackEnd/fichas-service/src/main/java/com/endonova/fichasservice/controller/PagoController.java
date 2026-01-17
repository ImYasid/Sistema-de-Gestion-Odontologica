package com.endonova.fichasservice.controller;

import com.endonova.fichasservice.model.Pago;
import com.endonova.fichasservice.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CONTROLADOR REST PARA PAGOS
 */
@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService service;

    // POST /pagos - Registrar pago
    @PostMapping
    public ResponseEntity<Pago> crear(@RequestBody Pago pago) {
        Pago nuevoPago = service.crear(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }

    // GET /pagos - Listar todos
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /pagos/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /pagos/ficha/{fichaTecnicaId} - Pagos de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}")
    public ResponseEntity<List<Pago>> buscarPorFicha(@PathVariable Long fichaTecnicaId) {
        return ResponseEntity.ok(service.buscarPorFicha(fichaTecnicaId));
    }

    // GET /pagos/ficha/{fichaTecnicaId}/total - Total pagado de una ficha
    @GetMapping("/ficha/{fichaTecnicaId}/total")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotal(@PathVariable Long fichaTecnicaId) {
        BigDecimal total = service.calcularTotalPagado(fichaTecnicaId);
        Map<String, BigDecimal> respuesta = new HashMap<>();
        respuesta.put("totalPagado", total);
        return ResponseEntity.ok(respuesta);
    }

    // PUT /pagos/{id} - Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        try {
            Pago pagoActualizado = service.actualizar(id, pago);
            return ResponseEntity.ok(pagoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /pagos/{id} - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
