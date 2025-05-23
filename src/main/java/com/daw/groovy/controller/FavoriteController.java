package com.daw.groovy.controller;

import com.daw.groovy.dto.SongDto;
import com.daw.groovy.entity.User;
import com.daw.groovy.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "User favorite songs management API")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @GetMapping
    @Operation(
        summary = "Get user's favorite songs",
        description = "Retrieves a list of songs that the authenticated user has marked as favorites"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved favorite songs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"title\":\"Just Dippin\",\"duration\":4.07,\"filePath\":\"/storage/songs/just-dippin_snoop-dogg.mp3\",\"albumId\":1,\"albumName\":\"Doggystyle\",\"artistName\":\"Snoop Dogg\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "User not found", ref = "NotFoundError")
    })
    public List<SongDto> getUserFavorites(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return favoriteService.getUserFavorites(user.getId());
    }
    
    @PostMapping("/{songId}")
    @Operation(
        summary = "Add song to favorites",
        description = "Adds a specific song to the authenticated user's favorites list"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Song successfully added to favorites",
            content = @Content(examples = @ExampleObject(value = "true"))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "User or song not found", ref = "NotFoundError")
    })
    public ResponseEntity<Boolean> addFavorite(@PathVariable Long songId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean added = favoriteService.addFavorite(user.getId(), songId);
        return ResponseEntity.ok(added);
    }
    
    @DeleteMapping("/{songId}")
    @Operation(
        summary = "Remove song from favorites",
        description = "Removes a specific song from the authenticated user's favorites list"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Song successfully removed from favorites",
            content = @Content(examples = @ExampleObject(value = "true"))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "User or song not found", ref = "NotFoundError")
    })
    public ResponseEntity<Boolean> removeFavorite(@PathVariable Long songId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean removed = favoriteService.removeFavorite(user.getId(), songId);
        return ResponseEntity.ok(removed);
    }
    
    @GetMapping("/status/{songId}")
    @Operation(
        summary = "Check if song is favorited",
        description = "Checks if a specific song is in the authenticated user's favorites list"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status retrieved successfully",
            content = @Content(examples = @ExampleObject(value = "true"))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError")
    })
    public ResponseEntity<Boolean> checkFavoriteStatus(@PathVariable Long songId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean isFavorite = favoriteService.isFavorite(user.getId(), songId);
        return ResponseEntity.ok(isFavorite);
    }
}
