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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.daw.groovy.dto.SongDto;
import com.daw.groovy.service.SongService;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@Tag(name = "Songs", description = "Song management API - Upload, stream, and manage songs")
@SecurityRequirement(name = "bearerAuth")
public class SongController {

    private final SongService songService;

    @GetMapping
    @Operation(
        summary = "Get all songs", 
        description = "Retrieves a list of all songs available in the system. This endpoint is paginated and returns up to 20 songs per request. Results include basic song information such as title, duration, and artist."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the list of songs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError")
    })
    public ResponseEntity<List<SongDto>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get song by ID", 
        description = "Retrieves detailed information about a specific song by its unique identifier. Returns complete song details including title, duration, album, and artist information."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the song",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "Song not found", ref = "NotFoundError")
    })
    public ResponseEntity<SongDto> getSongById(
            @Parameter(description = "ID of the song to retrieve - Must be a valid song ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping("/album/{albumId}")
    @Operation(
        summary = "Get songs by album", 
        description = "Retrieves all songs belonging to a specific album. Returns a list of songs with their details, ordered by track number if available."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the songs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "Album not found", ref = "NotFoundError")
    })
    public ResponseEntity<List<SongDto>> getSongsByAlbumId(
            @Parameter(description = "ID of the album to get songs from - Must be a valid album ID", required = true, example = "1")
            @PathVariable Long albumId) {
        return ResponseEntity.ok(songService.getSongsByAlbumId(albumId));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search songs", 
        description = "Search for songs by title. Returns a list of songs that match the search criteria. The search is case-insensitive and supports partial matches."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the matching songs",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError")
    })
    public ResponseEntity<List<SongDto>> searchSongsByTitle(
            @Parameter(description = "Title to search for - Can be partial match", required = true, example = "Billie")
            @RequestParam String title) {
        return ResponseEntity.ok(songService.searchSongsByTitle(title));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Create song", 
        description = "Upload a new song with metadata and audio file. Only administrators and artists can create songs. The audio file should be in MP3, WAV, or OGG format and not exceed 20MB."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully created the song",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data", ref = "ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError")
    })
    public ResponseEntity<SongDto> createSong(
            @Parameter(
                description = "Song metadata - Contains title, duration, and album ID", 
                required = true,
                schema = @Schema(implementation = SongDto.class),
                content = @Content(
                    examples = @ExampleObject(
                        value = "{\"title\":\"Billie Jean\",\"duration\":4.53,\"albumId\":1}"
                    )
                )
            )
            @RequestPart("song") @Valid SongDto songDto,
            @Parameter(
                description = "Audio file to upload - MP3, WAV, or OGG format, max 20MB", 
                required = false,
                content = @Content(mediaType = "audio/mpeg")
            )
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            @Parameter(
                description = "Custom filename for the audio file (optional)", 
                required = false
            )
            @RequestPart(value = "customFilename", required = false) String customFilename) {
        
        if (customFilename != null && !customFilename.isEmpty()) {
            return ResponseEntity.ok(songService.createSongWithCustomFilename(songDto, audioFile, customFilename));
        } else {
            return ResponseEntity.ok(songService.createSong(songDto, audioFile));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Update song", 
        description = "Update an existing song's metadata and optionally replace its audio file. Only administrators and the artist who created the song can update it. The audio file should be in MP3, WAV, or OGG format and not exceed 20MB."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully updated the song",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SongDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"title\":\"Billie Jean (Remix)\",\"duration\":5.10,\"filePath\":\"/storage/songs/1.mp3\",\"albumId\":1,\"albumName\":\"Thriller\",\"artistName\":\"Michael Jackson\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data", ref = "ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "Song not found", ref = "NotFoundError")
    })
    public ResponseEntity<SongDto> updateSong(
            @Parameter(
                description = "ID of the song to update - Must be a valid song ID", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                description = "Updated song metadata - Contains title, duration, and album ID", 
                required = true,
                schema = @Schema(implementation = SongDto.class),
                content = @Content(
                    examples = @ExampleObject(
                        value = "{\"title\":\"Billie Jean (Remix)\",\"duration\":5.10,\"albumId\":1}"
                    )
                )
            )
            @RequestPart("song") @Valid SongDto songDto,
            @Parameter(
                description = "New audio file to replace the existing one - MP3, WAV, or OGG format, max 20MB", 
                required = false,
                content = @Content(mediaType = "audio/mpeg")
            )
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile) {
        return ResponseEntity.ok(songService.updateSong(id, songDto, audioFile));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Delete song", 
        description = "Delete a song by its ID. Only administrators and the artist who created the song can delete it. This operation is irreversible and will remove the song file from storage."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the song"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "Song not found", ref = "NotFoundError")
    })
    public ResponseEntity<Void> deleteSong(
            @Parameter(
                description = "ID of the song to delete - Must be a valid song ID", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stream")
    @Operation(
        summary = "Stream song", 
        description = "Stream a song's audio file by its ID. Returns the binary audio data that can be played in a browser or media player. The response includes appropriate content type headers for audio playback."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the audio file",
            content = @Content(mediaType = "application/octet-stream")
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "Song not found", ref = "NotFoundError")
    })
    public ResponseEntity<byte[]> streamSong(
            @Parameter(
                description = "ID of the song to stream - Must be a valid song ID", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id) {
        SongDto song = songService.getSongById(id);
        byte[] audioData = songService.loadSongFile(song.getFilePath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audioData);
    }
}
