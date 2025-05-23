package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.groovy.dto.PlaylistDto;
import com.daw.groovy.entity.Playlist;
import com.daw.groovy.entity.Song;
import com.daw.groovy.entity.User;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.PlaylistMapper;
import com.daw.groovy.repository.PlaylistRepository;
import com.daw.groovy.repository.SongRepository;
import com.daw.groovy.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistMapper playlistMapper;
    
    public List<PlaylistDto> getAllPlaylists() {
        return playlistMapper.toDtoList(playlistRepository.findAll());
    }
    
    public PlaylistDto getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        return playlistMapper.toDto(playlist);
    }
    
    public List<PlaylistDto> getPlaylistsByUserId(Long userId) {
        return playlistMapper.toDtoList(playlistRepository.findByUserId(userId));
    }
    
    public List<PlaylistDto> searchPlaylistsByName(String name) {
        return playlistMapper.toDtoList(playlistRepository.findByNameContainingIgnoreCase(name));
    }
    
    @Transactional
    public PlaylistDto createPlaylist(PlaylistDto playlistDto) {
        User user = userRepository.findById(playlistDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + playlistDto.getUserId()));
        
        Playlist playlist = playlistMapper.toEntity(playlistDto);
        playlist.setUser(user);
        
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return playlistMapper.toDto(savedPlaylist);
    }
    
    @Transactional
    public PlaylistDto updatePlaylist(Long id, PlaylistDto playlistDto) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        
        playlistMapper.updateEntityFromDto(playlistDto, playlist);
        
        Playlist updatedPlaylist = playlistRepository.save(playlist);
        return playlistMapper.toDto(updatedPlaylist);
    }
    
    @Transactional
    public PlaylistDto addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + songId));
        
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            playlistRepository.save(playlist);
        }
        
        return playlistMapper.toDto(playlist);
    }
    
    @Transactional
    public PlaylistDto removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + songId));
        
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);
        
        return playlistMapper.toDto(playlist);
    }
    
    @Transactional
    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new ResourceNotFoundException("Playlist not found with id: " + id);
        }
        playlistRepository.deleteById(id);
    }
}
