package com.daw.groovy.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.daw.groovy.dto.PlaylistDto;
import com.daw.groovy.entity.Playlist;
import com.daw.groovy.repository.UserRepository;

import java.util.List;

@Mapper(
    componentModel = "spring", 
    uses = {SongMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PlaylistMapper {
    
    @Autowired
    private UserRepository userRepository;
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    public abstract PlaylistDto toDto(Playlist playlist);
    
    public abstract List<PlaylistDto> toDtoList(List<Playlist> playlists);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "songs", ignore = true)
    public abstract Playlist toEntity(PlaylistDto playlistDto);
    
    @AfterMapping
    protected void setUser(PlaylistDto playlistDto, @MappingTarget Playlist playlist) {
        if (playlistDto.getUserId() != null) {
            userRepository.findById(playlistDto.getUserId())
                .ifPresent(playlist::setUser);
        }
    }
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "songs", ignore = true)
    public abstract void updateEntityFromDto(PlaylistDto playlistDto, @MappingTarget Playlist playlist);
}
