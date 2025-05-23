package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/swagger-test")
@Tag(name = "Swagger Test", description = "Endpoints for testing Swagger UI functionality")
public class SwaggerController {

    @GetMapping("/ping")
    @Operation(
        summary = "Test endpoint", 
        description = "Simple endpoint to verify that the API and Swagger UI are working correctly"
    )
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "API is up and running",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
