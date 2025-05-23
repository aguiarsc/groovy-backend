package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.daw.groovy.dto.SongDto;
import com.daw.groovy.entity.Album;
import com.daw.groovy.entity.Playlist;
import com.daw.groovy.entity.Song;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.SongMapper;
import com.daw.groovy.repository.AlbumRepository;
import com.daw.groovy.repository.SongRepository;
import com.daw.groovy.repository.UserFavoriteRepository;
import com.daw.groovy.storage.StorageService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final SongMapper songMapper;
    private final StorageService storageService;
    private final UserFavoriteRepository userFavoriteRepository;
    
    public List<SongDto> getAllSongs() {
        return songMapper.toDtoList(songRepository.findAll());
    }
    
    public SongDto getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        return songMapper.toDto(song);
    }
    
    public List<SongDto> getSongsByAlbumId(Long albumId) {
        return songMapper.toDtoList(songRepository.findByAlbumId(albumId));
    }
    
    public List<SongDto> searchSongsByTitle(String title) {
        return songMapper.toDtoList(songRepository.findByTitleContainingIgnoreCase(title));
    }
    
    @Transactional
    public SongDto createSong(SongDto songDto, MultipartFile audioFile) {
        Album album = albumRepository.findById(songDto.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + songDto.getAlbumId()));
        
        Song song = songMapper.toEntity(songDto);
        song.setAlbum(album);
        
        if (audioFile != null && !audioFile.isEmpty()) {
            String filePath = storageService.store(audioFile);
            song.setFilePath(filePath);
        }
        
        Song savedSong = songRepository.save(song);
        return songMapper.toDto(savedSong);
    }
    
    @Transactional
    public SongDto createSongWithCustomFilename(SongDto songDto, MultipartFile audioFile, String customFilename) {
        Album album = albumRepository.findById(songDto.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + songDto.getAlbumId()));
        
        Song song = songMapper.toEntity(songDto);
        song.setAlbum(album);
        
        if (audioFile != null && !audioFile.isEmpty()) {
            String filePath = storageService.store(audioFile, customFilename);
            song.setFilePath(filePath);
        }
        
        Song savedSong = songRepository.save(song);
        return songMapper.toDto(savedSong);
    }
    
    @Transactional
    public SongDto updateSong(Long id, SongDto songDto, MultipartFile audioFile) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        
        if (songDto.getAlbumId() != null && !song.getAlbum().getId().equals(songDto.getAlbumId())) {
            Album album = albumRepository.findById(songDto.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + songDto.getAlbumId()));
            song.setAlbum(album);
        }
        
        songMapper.updateEntityFromDto(songDto, song);
        
        if (audioFile != null && !audioFile.isEmpty()) {
            // Delete old file if exists
            if (song.getFilePath() != null) {
                storageService.delete(song.getFilePath());
            }
            
            String filePath = storageService.store(audioFile);
            song.setFilePath(filePath);
        }
        
        Song updatedSong = songRepository.save(song);
        return songMapper.toDto(updatedSong);
    }
    
    @Transactional
    public void deleteSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));

        // Remove song from all users' favorites
        userFavoriteRepository.deleteBySongId(id);
        
        // Remove song from all playlists before deleting
        if (song.getPlaylists() != null && !song.getPlaylists().isEmpty()) {
            for (Playlist playlist : new ArrayList<>(song.getPlaylists())) {
                playlist.getSongs().remove(song);
            }
            song.getPlaylists().clear();
        }
        
        // Remove song from its album
        if (song.getAlbum() != null) {
            song.getAlbum().getSongs().remove(song);
        }
        
        // Delete audio file if exists
        if (song.getFilePath() != null) {
            storageService.delete(song.getFilePath());
        }
        
        songRepository.deleteById(id);
    }
    
    public byte[] loadSongFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new ResourceNotFoundException("Song file not found");
        }
        return storageService.loadAsResource(filePath);
    }
}
