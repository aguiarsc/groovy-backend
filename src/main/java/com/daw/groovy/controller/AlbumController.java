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

import com.daw.groovy.dto.AlbumDto;
import com.daw.groovy.service.AlbumService;
import com.daw.groovy.storage.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@Tag(name = "Albums", description = "Album management API - Create, retrieve, update, and delete music albums")
@SecurityRequirement(name = "bearerAuth")
public class AlbumController {

    private final AlbumService albumService;
    private final StorageService storageService;

    @GetMapping
    @Operation(
        summary = "Get all albums", 
        description = "Retrieves a list of all albums available in the system. Results include basic album information such as name, artist, and a list of songs if available."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the list of albums",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"name\":\"Thriller\",\"artistId\":1,\"artistName\":\"Michael Jackson\",\"songs\":[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53}]}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError")
    })
    public ResponseEntity<List<AlbumDto>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get album by ID", 
        description = "Retrieves detailed information about a specific album by its unique identifier. Returns complete album details including name, artist, and a list of songs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the album",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"name\":\"Thriller\",\"artistId\":1,\"artistName\":\"Michael Jackson\",\"songs\":[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53}]}"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "Album not found", ref = "NotFoundError")
    })
    public ResponseEntity<AlbumDto> getAlbumById(
            @Parameter(description = "ID of the album to retrieve - Must be a valid album ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @GetMapping("/artist/{artistId}")
    @Operation(
        summary = "Get albums by artist", 
        description = "Retrieves all albums created by a specific artist. Returns a list of albums with their details, including the songs in each album."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved the albums",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDto.class),
                examples = @ExampleObject(
                    value = "[{\"id\":1,\"name\":\"Thriller\",\"artistId\":1,\"artistName\":\"Michael Jackson\",\"songs\":[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53}]}]"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "404", description = "Artist not found", ref = "NotFoundError")
    })
    public ResponseEntity<List<AlbumDto>> getAlbumsByArtistId(
            @Parameter(description = "ID of the artist to get albums from - Must be a valid artist ID", required = true, example = "1")
            @PathVariable Long artistId) {
        return ResponseEntity.ok(albumService.getAlbumsByArtistId(artistId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Create album", 
        description = "Create a new album with the provided information. Only administrators and artists can create albums. The artist ID must reference an existing artist."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully created the album",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"name\":\"Thriller\",\"artistId\":1,\"artistName\":\"Michael Jackson\",\"songs\":[]}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data", ref = "ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError")
    })
    public ResponseEntity<AlbumDto> createAlbum(
            @Parameter(
                description = "Album information - Contains name and artist ID", 
                required = true,
                schema = @Schema(implementation = AlbumDto.class),
                content = @Content(
                    examples = @ExampleObject(
                        value = "{\"name\":\"Thriller\",\"artistId\":1}"
                    )
                )
            )
            @Valid @RequestBody AlbumDto albumDto) {
        return ResponseEntity.ok(albumService.createAlbum(albumDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Update album", 
        description = "Update an existing album's information. Only administrators and the artist who created the album can update it. The artist ID must reference an existing artist."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully updated the album",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlbumDto.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"name\":\"Thriller (Remastered)\",\"artistId\":1,\"artistName\":\"Michael Jackson\",\"songs\":[{\"id\":1,\"title\":\"Billie Jean\",\"duration\":4.53}]}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data", ref = "ValidationError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "Album not found", ref = "NotFoundError")
    })
    public ResponseEntity<AlbumDto> updateAlbum(
            @Parameter(
                description = "ID of the album to update - Must be a valid album ID", 
                required = true,
                example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                description = "Updated album information - Contains name and artist ID", 
                required = true,
                schema = @Schema(implementation = AlbumDto.class),
                content = @Content(
                    examples = @ExampleObject(
                        value = "{\"name\":\"Thriller (Remastered)\",\"artistId\":1}"
                    )
                )
            )
            @Valid @RequestBody AlbumDto albumDto) {
        return ResponseEntity.ok(albumService.updateAlbum(id, albumDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Delete album", 
        description = "Delete an album by its ID. Only administrators and the artist who created the album can delete it. This operation is irreversible and will remove all associated songs from the album."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the album"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions", ref = "ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "Album not found", ref = "NotFoundError")
    })
    public ResponseEntity<Void> deleteAlbum(
            @Parameter(
                description = "ID of the album to delete - Must be a valid album ID", 
                required = true,
                example = "1"
            )
            @PathVariable Long id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'ARTIST')")
    @Operation(
        summary = "Upload album cover image", 
        description = "Uploads a cover image for a specific album. Only the album's artist or an admin can upload a cover image."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully uploaded the album cover image",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "\"album1.jpg\""
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid file or album ID", ref = "BadRequestError"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", ref = "UnauthorizedError"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User is not the album's artist or an admin", ref = "ForbiddenError"),
        @ApiResponse(responseCode = "404", description = "Album not found", ref = "NotFoundError")
    })
    public ResponseEntity<String> uploadAlbumCover(
            @Parameter(description = "ID of the album to upload a cover for", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Cover image file to upload (JPEG or PNG)", required = true)
            @RequestParam("file") MultipartFile file) {
        
        // Verify album exists
        AlbumDto album = albumService.getAlbumById(id);
        
        // Store the file with a predictable name for easier retrieval
        String filename = "album" + id + "." + getFileExtension(file.getOriginalFilename());
        storageService.store(file, filename);
        
        // Update the album with the cover image filename
        album.setCoverImage(filename);
        albumService.updateAlbum(id, album);
        
        return ResponseEntity.ok(filename);
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
