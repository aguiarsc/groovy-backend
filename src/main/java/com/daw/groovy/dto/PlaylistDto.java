package com.daw.groovy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Playlist information")
public class PlaylistDto {
    
    @Schema(description = "Playlist ID - automatically generated", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "Playlist name is required")
    @Size(min = 1, max = 100, message = "Playlist name must be between 1 and 100 characters")
    @Schema(description = "Playlist name", example = "My Favorite Songs", required = true)
    private String name;
    
    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who created this playlist", example = "1", required = true)
    private Long userId;
    
    @Schema(description = "Name of the user who created this playlist (read-only, populated automatically)", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userName;
    
    @Builder.Default
    @Schema(description = "List of songs in this playlist", required = false)
    private List<SongDto> songs = new ArrayList<>();
}
