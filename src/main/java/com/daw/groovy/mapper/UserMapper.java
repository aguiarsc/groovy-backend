package com.daw.groovy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.daw.groovy.dto.UserDto;
import com.daw.groovy.entity.User;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    
    UserDto toDto(User user);
    
    List<UserDto> toDtoList(List<User> users);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    User toEntity(UserDto userDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    void updateEntityFromDto(UserDto userDto, @MappingTarget User user);
}
