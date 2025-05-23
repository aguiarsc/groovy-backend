package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.groovy.dto.AlbumDto;
import com.daw.groovy.entity.Album;
import com.daw.groovy.entity.Artist;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.AlbumMapper;
import com.daw.groovy.repository.AlbumRepository;
import com.daw.groovy.repository.ArtistRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {
    
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumMapper albumMapper;
    
    public List<AlbumDto> getAllAlbums() {
        return albumMapper.toDtoList(albumRepository.findAll());
    }
    
    public AlbumDto getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
        return albumMapper.toDto(album);
    }
    
    public List<AlbumDto> getAlbumsByArtistId(Long artistId) {
        return albumMapper.toDtoList(albumRepository.findByArtistId(artistId));
    }
    
    @Transactional
    public AlbumDto createAlbum(AlbumDto albumDto) {
        Artist artist = artistRepository.findById(albumDto.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + albumDto.getArtistId()));
        
        Album album = albumMapper.toEntity(albumDto);
        album.setArtist(artist);
        
        Album savedAlbum = albumRepository.save(album);
        return albumMapper.toDto(savedAlbum);
    }
    
    @Transactional
    public AlbumDto updateAlbum(Long id, AlbumDto albumDto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
        
        if (albumDto.getArtistId() != null && !album.getArtist().getId().equals(albumDto.getArtistId())) {
            Artist artist = artistRepository.findById(albumDto.getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + albumDto.getArtistId()));
            album.setArtist(artist);
        }
        
        albumMapper.updateEntityFromDto(albumDto, album);
        Album updatedAlbum = albumRepository.save(album);
        return albumMapper.toDto(updatedAlbum);
    }
    
    @Transactional
    public void deleteAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with id: " + id));
        
        // Check if album has songs
        if (album.getSongs() != null && !album.getSongs().isEmpty()) {
            throw new IllegalStateException("Cannot delete album with songs. Remove all songs first.");
        }
        
        albumRepository.deleteById(id);
    }
}
