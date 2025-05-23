package com.daw.groovy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Groovy",
                description = "API for a music application that allows users to browse, search, and listen to music. " +
                        "Users can create playlists, follow artists, and manage their profiles. " +
                        "Artists can upload songs, create albums, and manage their content.",
                version = "1.0",
                contact = @Contact(
                        name = "Groovy Team",
                        email = "support@groovy.com",
                        url = "https://github.com/AguiarSC/groovy"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Development Environment",
                        url = "https://dev-api.musicapp.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication. Use the /api/auth/login endpoint to obtain a token.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addResponses("UnauthorizedError", createUnauthorizedResponse())
                        .addResponses("ForbiddenError", createForbiddenResponse())
                        .addResponses("NotFoundError", createNotFoundResponse())
                        .addResponses("ValidationError", createValidationErrorResponse())
                        .addResponses("ServerError", createServerErrorResponse())
                        .addSchemas("ErrorResponse", createErrorResponseSchema())
                );
    }

    private Schema<Object> createErrorResponseSchema() {
        Schema<Object> errorResponseSchema = new Schema<>();
        errorResponseSchema.setType("object");
        errorResponseSchema.setDescription("Standard error response format");
        
        Schema<Object> validationErrorSchema = new Schema<>();
        validationErrorSchema.setType("object");
        validationErrorSchema.setDescription("Validation error details");
        
        Schema<String> fieldSchema = new Schema<>();
        fieldSchema.setType("string");
        fieldSchema.setExample("title");
        validationErrorSchema.addProperty("field", fieldSchema);
        
        Schema<String> messageSchema = new Schema<>();
        messageSchema.setType("string");
        messageSchema.setExample("Title cannot be empty");
        validationErrorSchema.addProperty("message", messageSchema);
        
        Schema<String> errorMessageSchema = new Schema<>();
        errorMessageSchema.setType("string");
        errorMessageSchema.setExample("An error occurred while processing your request");
        errorResponseSchema.addProperty("message", errorMessageSchema);
        
        Schema<Integer> statusSchema = new Schema<>();
        statusSchema.setType("integer");
        statusSchema.setExample(400);
        errorResponseSchema.addProperty("status", statusSchema);
        
        Schema<String> timestampSchema = new Schema<>();
        timestampSchema.setType("string");
        timestampSchema.setFormat("date-time");
        timestampSchema.setExample("2025-03-13T22:10:53");
        errorResponseSchema.addProperty("timestamp", timestampSchema);
        
        Schema<String> pathSchema = new Schema<>();
        pathSchema.setType("string");
        pathSchema.setExample("/api/songs/123");
        errorResponseSchema.addProperty("path", pathSchema);
        
        Schema<Object> errorsSchema = new Schema<>();
        errorsSchema.setType("array");
        errorsSchema.setItems(validationErrorSchema);
        errorResponseSchema.addProperty("errors", errorsSchema);
        
        return errorResponseSchema;
    }

    private ApiResponse createUnauthorizedResponse() {
        return new ApiResponse()
                .description("Unauthorized - Authentication is required to access this resource")
                .content(createErrorContent("Invalid or missing authentication token"));
    }

    private ApiResponse createForbiddenResponse() {
        return new ApiResponse()
                .description("Forbidden - You don't have permission to access this resource")
                .content(createErrorContent("You don't have the necessary permissions to perform this action"));
    }

    private ApiResponse createNotFoundResponse() {
        return new ApiResponse()
                .description("Not Found - The requested resource was not found")
                .content(createErrorContent("The requested resource does not exist"));
    }

    private ApiResponse createValidationErrorResponse() {
        return new ApiResponse()
                .description("Bad Request - Validation error")
                .content(createErrorContent("Invalid request parameters or payload"));
    }

    private ApiResponse createServerErrorResponse() {
        return new ApiResponse()
                .description("Internal Server Error")
                .content(createErrorContent("An unexpected error occurred on the server"));
    }

    private Content createErrorContent(String message) {
        Map<String, Example> examples = new HashMap<>();
        examples.put("error", new Example().value(Map.of(
                "message", message,
                "status", 400,
                "timestamp", "2025-03-13T22:10:53",
                "path", "/api/songs/123",
                "errors", new Object[0]
        )));

        return new Content()
                .addMediaType("application/json", new MediaType()
                        .schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
                        .examples(examples));
    }
}
