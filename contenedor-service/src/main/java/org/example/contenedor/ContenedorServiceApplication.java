package org.example.contenedor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ContenedorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContenedorServiceApplication.class, args);
    }
}