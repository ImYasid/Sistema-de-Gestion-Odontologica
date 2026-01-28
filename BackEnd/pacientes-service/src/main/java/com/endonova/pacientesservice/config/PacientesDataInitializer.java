package com.endonova.pacientesservice.config;

import com.endonova.pacientesservice.model.Paciente;
import com.endonova.pacientesservice.repository.PacienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Inicializador de datos para verificar tabla pacientes
 */
@Configuration
public class PacientesDataInitializer {

    @Bean
    CommandLineRunner initPacientes(PacienteRepository pacienteRepository) {
        return args -> {
            try {
                Long count = pacienteRepository.count();
                System.out.println("✓ Tabla 'pacientes' está operativa. Registros: " + count);
            } catch (Exception e) {
                System.out.println("⚠ Error verificando tabla pacientes: " + e.getMessage());
            }
        };
    }
}
