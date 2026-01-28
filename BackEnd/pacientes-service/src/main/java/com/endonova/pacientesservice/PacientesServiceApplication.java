package com.endonova.pacientesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.endonova.pacientesservice.repository")
@ComponentScan(basePackages = {"com.endonova.pacientesservice"})
public class PacientesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacientesServiceApplication.class, args);
    }

}
