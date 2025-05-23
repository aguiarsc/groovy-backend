package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.daw.groovy.dto.ArtistDto;
import com.daw.groovy.service.ArtistService;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
@Tag(name = "Artists", description = "Artist management API")
@SecurityRequirement(name = "bearerAuth")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    @Operation(summary = "Get all artists", description = "Get a list of all artists")
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID", description = "Get an artist by their ID")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistService.getArtistById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create artist", description = "Create a new artist (Admin only)")
    public ResponseEntity<ArtistDto> createArtist(@Valid @RequestBody ArtistDto artistDto) {
        return ResponseEntity.ok(artistService.createArtist(artistDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(summary = "Update artist", description = "Update an existing artist (Admin or the artist themselves)")
    public ResponseEntity<ArtistDto> updateArtist(
            @PathVariable Long id,
            @Valid @RequestBody ArtistDto artistDto) {
        return ResponseEntity.ok(artistService.updateArtist(id, artistDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete artist", description = "Delete an artist by their ID (Admin only)")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search artists", description = "Search for artists by name")
    public ResponseEntity<List<ArtistDto>> searchArtists(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(artistService.searchArtistsByName(name));
        }
        return ResponseEntity.ok(artistService.getAllArtists());
    }
}
