package com.dasa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe Principal do Spring Boot - REST API DASA
 * Console e Swing continuam funcionando independentemente através de suas próprias classes Main.
 */
@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    DASA - REST API INICIANDO...");
        System.out.println("=================================================");

        // Inicia o Spring Boot
        SpringApplication.run(ApiApplication.class, args);

        System.out.println("=================================================");
        System.out.println("    REST API INICIADA COM SUCESSO!");
        System.out.println("    Swagger: http://localhost:8080/swagger-ui");
        System.out.println("=================================================");
    }
}
