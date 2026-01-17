package com.endonova.pacientesservice.controller;

import com.endonova.pacientesservice.model.Paciente;
import com.endonova.pacientesservice.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*") // Permitir la conexión desde React
public class PacienteController {

    @Autowired
    private PacienteService Pservice;

    // Listar todos los pacientes
    // GET http://localhost:8081/pacientes
    @GetMapping
    public List<Paciente> getAllPacientes() {
        return Pservice.listarTodos();
    }

    // Guardar paciente
    // POST http://localhost:8081/pacientes
    @PostMapping
    public Paciente guardarPaciente(@RequestBody Paciente paciente){
        return Pservice.guardarPaciente(paciente);
    }

    // Buscar paciente por ID
    // GET http://localhost:8081/pacientes/1
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return Pservice.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Buscar paciente por cedula
    // GET http://localhost:8081/pacientes/buscar/1720055555
    @GetMapping("/buscar/{cedula}")
    public ResponseEntity<Paciente> obtenerPorCedula(@PathVariable String cedula) {
        return Pservice.buscarPorCedula(cedula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar paciente
    // DELETE http://localhost:8081/pacientes/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Pservice.eliminarPaciente(id);
        return ResponseEntity.ok().build();
    }

}
