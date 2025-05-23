package com.daw.groovy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Schema(description = "Album information - Contains details about a music album and its songs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDto {
    
    @Schema(
        description = "Album ID - automatically generated", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "1",
        title = "Album identifier"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "Album name is required")
    @Size(min = 1, max = 100, message = "Album name must be between 1 and 100 characters")
    @Schema(
        description = "Album name - The title of the album", 
        example = "Thriller", 
        required = true,
        title = "Album name"
    )
    private String name;
    
    @NotNull(message = "Artist ID is required")
    @Schema(
        description = "ID of the artist who created this album - Must reference an existing artist", 
        example = "1", 
        required = true,
        title = "Artist ID"
    )
    private Long artistId;
    
    @Schema(
        description = "Name of the artist (read-only, populated automatically) - The name of the artist who created this album", 
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "Michael Jackson",
        title = "Artist name"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String artistName;
    
    @Schema(
        description = "Cover image filename - The filename of the album cover image", 
        example = "album1.jpg",
        title = "Cover Image"
    )
    private String coverImage;
    
    @Builder.Default
    @Schema(
        description = "List of songs in this album (read-only, populated automatically) - Collection of songs that belong to this album", 
        accessMode = Schema.AccessMode.READ_ONLY,
        title = "Songs"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<SongDto> songs = new ArrayList<>();
}
