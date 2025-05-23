package com.daw.groovy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Song information - Contains details about a music track")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongDto {
    
    @Schema(
        description = "Song ID - automatically generated", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "1",
        title = "Song identifier"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "Song title is required")
    @Size(min = 1, max = 100, message = "Song title must be between 1 and 100 characters")
    @Schema(
        description = "Song title - The name of the track", 
        example = "Billie Jean", 
        required = true,
        title = "Title"
    )
    private String title;
    
    @Positive(message = "Duration must be positive")
    @Schema(
        description = "Song duration in minutes - How long the track plays", 
        example = "4.53", 
        required = false,
        title = "Duration"
    )
    private Double duration;
    
    @Schema(
        description = "Path to the audio file (read-only, populated automatically) - Internal storage location", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "/storage/songs/1/song.mp3",
        title = "File path"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String filePath;
    
    @NotNull(message = "Album ID is required")
    @Schema(
        description = "ID of the album this song belongs to - Must reference an existing album", 
        example = "1", 
        required = true,
        title = "Album ID"
    )
    private Long albumId;
    
    @Schema(
        description = "Album name (read-only, populated automatically) - The name of the album containing this song", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "Thriller",
        title = "Album name"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String albumName;
    
    @Schema(
        description = "Artist name (read-only, populated automatically) - The name of the artist who created this song", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "Michael Jackson",
        title = "Artist name"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String artistName;
}
