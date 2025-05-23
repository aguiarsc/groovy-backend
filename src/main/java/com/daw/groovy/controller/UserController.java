package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.daw.groovy.dto.UserDto;
import com.daw.groovy.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API for retrieving, updating, and deleting user accounts")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all users", 
        description = "Retrieves a list of all users in the system. This endpoint is restricted to administrators only."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the list of users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "#/components/responses/UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", ref = "#/components/responses/ForbiddenError")
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID", 
        description = "Retrieves detailed information about a specific user by their unique identifier. Users can only access their own information unless they are administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "#/components/responses/UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", ref = "#/components/responses/ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "User not found", ref = "#/components/responses/NotFoundError")
    })
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current authenticated user", 
        description = "Retrieves the profile information of the currently authenticated user. This endpoint uses the JWT token to identify the user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the current user profile",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token", ref = "#/components/responses/UnauthorizedError")
    })
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentAuthenticatedUser());
    }


    @PutMapping("/{id}")
    @Operation(
        summary = "Update user", 
        description = "Updates an existing user's information. Users can only update their own information unless they are administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully updated the user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data", ref = "#/components/responses/ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "#/components/responses/UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions", ref = "#/components/responses/ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "User not found", ref = "#/components/responses/NotFoundError")
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID of the user to update", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated user information",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Update User Example",
                            summary = "Example of updating a user's name and email",
                            value = "{\n" +
                                   "  \"name\": \"Updated Name\",\n" +
                                   "  \"email\": \"updated.email@example.com\",\n" +
                                   "  \"password\": \"newPassword123\"\n" +
                                   "}"
                        )
                    }
                )
            )
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete user", 
        description = "Deletes a user account by their ID. This endpoint is restricted to administrators only."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "#/components/responses/UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", ref = "#/components/responses/ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "User not found", ref = "#/components/responses/NotFoundError")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true, example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new user", 
        description = "Creates a new user account. This endpoint is restricted to administrators only."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Successfully created the user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data or email already in use", ref = "#/components/responses/ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "#/components/responses/UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", ref = "#/components/responses/ForbiddenError")
    })
    public ResponseEntity<UserDto> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "New user information",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Create User Example",
                            summary = "Example of creating a new user",
                            value = "{\n" +
                                   "  \"name\": \"New User\",\n" +
                                   "  \"email\": \"new.user@example.com\",\n" +
                                   "  \"password\": \"password123\",\n" +
                                   "  \"role\": \"USER\"\n" +
                                   "}"
                        )
                    }
                )
            )
            @Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }
}
