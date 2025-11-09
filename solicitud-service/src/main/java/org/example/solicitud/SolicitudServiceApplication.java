package org.example.solicitud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SolicitudServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolicitudServiceApplication.class, args);
    }
}