package com.daw.groovy.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.daw.groovy.dto.AlbumDto;
import com.daw.groovy.entity.Album;
import com.daw.groovy.repository.ArtistRepository;

import java.util.List;

@Mapper(
    componentModel = "spring", 
    uses = {SongMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AlbumMapper {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Mapping(target = "artistId", source = "artist.id")
    @Mapping(target = "artistName", source = "artist.name")
    public abstract AlbumDto toDto(Album album);
    
    public abstract List<AlbumDto> toDtoList(List<Album> albums);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "songs", ignore = true)
    public abstract Album toEntity(AlbumDto albumDto);
    
    @AfterMapping
    protected void setArtist(AlbumDto albumDto, @MappingTarget Album album) {
        if (albumDto.getArtistId() != null) {
            artistRepository.findById(albumDto.getArtistId())
                .ifPresent(album::setArtist);
        }
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "songs", ignore = true)
    public abstract void updateEntityFromDto(AlbumDto albumDto, @MappingTarget Album album);
}
