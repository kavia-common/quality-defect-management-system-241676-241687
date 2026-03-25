package com.example.backendapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot entrypoint for the Quality Defect Management System backend API.
 *
 * Exposes REST APIs under /api/* for users, defects, RCA, corrective actions, dashboard aggregations,
 * and export utilities.
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Quality Defect Management System API",
                version = "0.1.0",
                description = "REST API for managing defects, RCA (5-Whys), corrective actions, dashboards, and PDF exports."
        )
)
@Tag(name = "System", description = "System/health/documentation endpoints")
public class BackendapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendapiApplication.class, args);
    }
}
