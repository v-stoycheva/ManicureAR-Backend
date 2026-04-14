package com.manicurear.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        // Тази линия стартира целия Spring контекст, свързва се с базата и пуска Tomcat сървъра
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("✅ ManicureAR Backend стартира успешно на порт 8080!");
    }
}