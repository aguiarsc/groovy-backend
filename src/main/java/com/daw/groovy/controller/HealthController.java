package com.daw.groovy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple controller for Docker health checks
 * 
 * Provides a basic endpoint that Docker can use to verify
 * if the application is running properly
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {
    
    /**
     * Basic health check endpoint
     * 
     * @return Status information with timestamp
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", new Date().toString());
        return ResponseEntity.ok(status);
    }
}