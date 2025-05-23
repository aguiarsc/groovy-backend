package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.groovy.dto.AuthRequest;
import com.daw.groovy.dto.AuthResponse;
import com.daw.groovy.dto.UserDto;
import com.daw.groovy.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API for user registration and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Register a new user", 
        description = "Register a new user with the provided details. The ID field should NOT be provided as it will be auto-generated."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User successfully registered",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input or email already in use",
            ref = "#/components/responses/ValidationError"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            ref = "#/components/responses/ServerError"
        )
    })
    public ResponseEntity<AuthResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User registration details",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Standard User",
                            summary = "Register a standard user",
                            value = "{\n" +
                                   "  \"name\": \"John Doe\",\n" +
                                   "  \"email\": \"john.doe@example.com\",\n" +
                                   "  \"password\": \"securePassword123\"\n" +
                                   "}"
                        ),
                        @ExampleObject(
                            name = "Admin User",
                            summary = "Register an admin user",
                            value = "{\n" +
                                   "  \"name\": \"Admin User\",\n" +
                                   "  \"email\": \"admin@example.com\",\n" +
                                   "  \"role\": \"ADMIN\",\n" +
                                   "  \"password\": \"adminPassword123\"\n" +
                                   "}"
                        )
                    }
                )
            )
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.register(userDto));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate a user", 
        description = "Authenticate a user with email and password to obtain a JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Authentication successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            ref = "#/components/responses/UnauthorizedError"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Internal server error",
            ref = "#/components/responses/ServerError"
        )
    })
    public ResponseEntity<AuthResponse> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User credentials",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthRequest.class),
                    examples = {
                        @ExampleObject(
                            name = "Standard Login",
                            summary = "Login with email and password",
                            value = "{\n" +
                                   "  \"email\": \"john.doe@example.com\",\n" +
                                   "  \"password\": \"securePassword123\"\n" +
                                   "}"
                        )
                    }
                )
            )
            @Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
