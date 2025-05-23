package com.daw.groovy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Artist information - extends UserDto with artist-specific fields")
public class ArtistDto extends UserDto {
    
    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    @Schema(description = "Artist biography", example = "Grammy-winning artist with multiple platinum albums")
    private String biography;
    
    @Schema(description = "URL to the artist's profile picture", example = "https://example.com/images/artist1.jpg")
    private String profilePicture;

    @Schema(description = "List of albums associated with the artist")
    private List<AlbumDto> albums;

    @Schema(description = "List of songs associated with the artist")
    private List<SongDto> songs;
}