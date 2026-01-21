package com.endonova.odontogramaservice.controller;

import com.endonova.odontogramaservice.model.Diente;
import com.endonova.odontogramaservice.services.DienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/odontograma")
@CrossOrigin("*") // Permite conexión desde cualquier Frontend
public class OdontogramaController {

    @Autowired
    private DienteService servicio;

    // GET: http://localhost:8084/api/odontograma/{pacienteId}
    @GetMapping("/{pacienteId}")
    public List<Diente> obtenerDientes(@PathVariable Long pacienteId) {
        return servicio.listarPorPaciente(pacienteId);
    }

    // POST: http://localhost:8084/api/odontograma
    @PostMapping
    public Diente guardarDiente(@RequestBody Diente diente) {
        return servicio.guardar(diente);
    }
}