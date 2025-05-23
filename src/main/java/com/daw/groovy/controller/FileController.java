package com.daw.groovy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.daw.groovy.storage.StorageService;
import com.daw.groovy.exception.StorageFileNotFoundException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File serving API")
public class FileController {

    private final StorageService storageService;

    @GetMapping(value = "/{filename}")
    @Operation(
        summary = "Get file by filename",
        description = "Retrieve a file by its filename. Returns the file content directly."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "File successfully retrieved",
            content = @Content(mediaType = "application/octet-stream")
        ),
        @ApiResponse(responseCode = "404", description = "File not found"),
        @ApiResponse(responseCode = "206", description = "Partial content (for range requests)")
    })
    public ResponseEntity<byte[]> getFile(
            @Parameter(description = "Filename to retrieve", required = true)
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            byte[] fileContent = storageService.loadAsResource(filename);
            MediaType mediaType = determineMediaType(filename);
            
            // If no range header, return the full file
            if (rangeHeader == null) {
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header("Accept-Ranges", "bytes")
                        .contentLength(fileContent.length)
                        .body(fileContent);
            }
            
            // Parse range header (e.g., "bytes=0-1023")
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            int start = Integer.parseInt(ranges[0]);
            int end = ranges.length > 1 && !ranges[1].isEmpty() 
                    ? Integer.parseInt(ranges[1]) 
                    : fileContent.length - 1;
            
            // Ensure end is not beyond file length
            end = Math.min(end, fileContent.length - 1);
            int contentLength = end - start + 1;
            
            // Create a byte array for the requested range
            byte[] rangeContent = new byte[contentLength];
            System.arraycopy(fileContent, start, rangeContent, 0, contentLength);
            
            // Return partial content (206)
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(mediaType)
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", String.format("bytes %d-%d/%d", start, end, fileContent.length))
                    .contentLength(contentLength)
                    .body(rangeContent);
        } catch (StorageFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Invalid range header
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }
    }
    
    private MediaType determineMediaType(String filename) {
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.toLowerCase().endsWith(".mp3")) {
            return MediaType.parseMediaType("audio/mpeg");
        } else if (filename.toLowerCase().endsWith(".wav")) {
            return MediaType.parseMediaType("audio/wav");
        } else if (filename.toLowerCase().endsWith(".ogg")) {
            return MediaType.parseMediaType("audio/ogg");
        } else if (filename.toLowerCase().endsWith(".flac")) {
            return MediaType.parseMediaType("audio/flac");
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
