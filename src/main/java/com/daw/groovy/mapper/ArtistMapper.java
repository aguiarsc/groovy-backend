package com.daw.groovy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.daw.groovy.dto.ArtistDto;
import com.daw.groovy.entity.Artist;

import java.util.List;

@Mapper(
    componentModel = "spring", 
    uses = {AlbumMapper.class, SongMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ArtistMapper {
    
    @Mapping(target = "albums", source = "albums")
    @Mapping(target = "songs", source = "songs")
    public abstract ArtistDto toDto(Artist artist);
    
    public abstract List<ArtistDto> toDtoList(List<Artist> artists);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract Artist toEntity(ArtistDto artistDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract void updateEntityFromDto(ArtistDto artistDto, @MappingTarget Artist artist);
}