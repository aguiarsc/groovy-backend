package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.groovy.dto.PlaylistDto;
import com.daw.groovy.service.PlaylistService;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
@Tag(name = "Playlists", description = "Playlist management API")
@SecurityRequirement(name = "bearerAuth")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    @Operation(summary = "Get all playlists", description = "Get a list of all playlists")
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get playlist by ID", description = "Get a playlist by its ID")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getPlaylistById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get playlists by user", description = "Get all playlists created by a specific user")
    public ResponseEntity<List<PlaylistDto>> getPlaylistsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(playlistService.getPlaylistsByUserId(userId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search playlists", description = "Search playlists by name")
    public ResponseEntity<List<PlaylistDto>> searchPlaylistsByName(@RequestParam String name) {
        return ResponseEntity.ok(playlistService.searchPlaylistsByName(name));
    }

    @PostMapping
    @Operation(summary = "Create playlist", description = "Create a new playlist")
    public ResponseEntity<PlaylistDto> createPlaylist(@Valid @RequestBody PlaylistDto playlistDto) {
        return ResponseEntity.ok(playlistService.createPlaylist(playlistDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update playlist", description = "Update an existing playlist")
    public ResponseEntity<PlaylistDto> updatePlaylist(
            @PathVariable Long id,
            @Valid @RequestBody PlaylistDto playlistDto) {
        return ResponseEntity.ok(playlistService.updatePlaylist(id, playlistDto));
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    @Operation(summary = "Add song to playlist", description = "Add a song to a playlist")
    public ResponseEntity<PlaylistDto> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.addSongToPlaylist(playlistId, songId));
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    @Operation(summary = "Remove song from playlist", description = "Remove a song from a playlist")
    public ResponseEntity<PlaylistDto> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.removeSongFromPlaylist(playlistId, songId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete playlist", description = "Delete a playlist by its ID")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
}
