package com.endonova.odontogramaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.endonova.odontogramaservice.repository")
@ComponentScan(basePackages = {"com.endonova.odontogramaservice"})
public class OdontogramaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdontogramaServiceApplication.class, args);
    }

}
