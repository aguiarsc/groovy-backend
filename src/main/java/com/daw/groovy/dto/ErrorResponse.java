package com.daw.groovy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorResponse {

    @Schema(description = "Error message", example = "An error occurred while processing your request")
    private String message;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error timestamp", example = "2025-03-13T22:10:53")
    private LocalDateTime timestamp;

    @Schema(description = "Request path that caused the error", example = "/api/songs/123")
    private String path;

    @Schema(description = "List of validation errors (if applicable)")
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Validation error details")
    public static class ValidationError {
        
        @Schema(description = "Field that failed validation", example = "title")
        private String field;
        
        @Schema(description = "Validation error message", example = "Title cannot be empty")
        private String message;
    }
}
