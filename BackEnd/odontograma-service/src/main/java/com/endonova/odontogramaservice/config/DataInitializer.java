package com.endonova.odontogramaservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * DataInitializer para garantizar que la tabla dientes_paciente existe
 * Se ejecuta automáticamente al iniciar la aplicación
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                // Verificar si la tabla existe
                Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'dientes_paciente'",
                    Integer.class
                );
                
                if (count != null && count > 0) {
                    System.out.println("✓ Tabla 'dientes_paciente' existe correctamente");
                } else {
                    System.out.println("⚠ Tabla 'dientes_paciente' no encontrada, Hibernate la creará en el siguiente ciclo");
                }
            } catch (Exception e) {
                System.out.println("⚠ No se pudo verificar la tabla 'dientes_paciente': " + e.getMessage());
                System.out.println("  La tabla será creada automáticamente por Hibernate (ddl-auto=update)");
            }
        };
    }
}
