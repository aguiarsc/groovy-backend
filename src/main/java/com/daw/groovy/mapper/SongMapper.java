package com.daw.groovy.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.daw.groovy.dto.SongDto;
import com.daw.groovy.entity.Song;
import com.daw.groovy.repository.AlbumRepository;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SongMapper {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Mapping(target = "albumId", source = "album.id")
    @Mapping(target = "albumName", source = "album.name")
    @Mapping(target = "artistName", source = "album.artist.name")
    public abstract SongDto toDto(Song song);
    
    public abstract List<SongDto> toDtoList(List<Song> songs);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract Song toEntity(SongDto songDto);
    
    @AfterMapping
    protected void setAlbum(SongDto songDto, @MappingTarget Song song) {
        if (songDto.getAlbumId() != null) {
            albumRepository.findById(songDto.getAlbumId())
                .ifPresent(song::setAlbum);
        }
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract void updateEntityFromDto(SongDto songDto, @MappingTarget Song song);
}
