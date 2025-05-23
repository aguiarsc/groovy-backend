package com.daw.groovy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.groovy.dto.ArtistDto;
import com.daw.groovy.entity.Artist;
import com.daw.groovy.enums.Role;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.ArtistMapper;
import com.daw.groovy.repository.ArtistRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArtistService {
    
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final PasswordEncoder passwordEncoder;
    
    public List<ArtistDto> getAllArtists() {
        List<Artist> artistsWithAlbums = artistRepository.findAllWithAlbums();
        List<Artist> artistsWithSongs = artistRepository.findAllWithSongs();
    
        Map<Long, Artist> artistMap = new HashMap<>();
        artistsWithAlbums.forEach(artist -> artistMap.put(artist.getId(), artist));
        artistsWithSongs.forEach(artist -> {
            Artist existingArtist = artistMap.get(artist.getId());
            if (existingArtist != null) {
                existingArtist.setSongs(artist.getSongs());
            }
        });
    
        return artistMapper.toDtoList(new ArrayList<>(artistMap.values()));
    }
    
    public ArtistDto getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
        return artistMapper.toDto(artist);
    }
    
    @Transactional
    public ArtistDto createArtist(ArtistDto artistDto) {
        Artist artist = artistMapper.toEntity(artistDto);
        artist.setRole(Role.ARTIST);
        
        if (artistDto.getPassword() != null) {
            artist.setPassword(passwordEncoder.encode(artistDto.getPassword()));
        }
        
        Artist savedArtist = artistRepository.save(artist);
        return artistMapper.toDto(savedArtist);
    }
    
    @Transactional
    public ArtistDto updateArtist(Long id, ArtistDto artistDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
        
        artistMapper.updateEntityFromDto(artistDto, artist);
        
        if (artistDto.getPassword() != null) {
            artist.setPassword(passwordEncoder.encode(artistDto.getPassword()));
        }
        
        Artist updatedArtist = artistRepository.save(artist);
        return artistMapper.toDto(updatedArtist);
    }
    
    @Transactional
    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
        
        // Check if artist has albums
        if (artist.getAlbums() != null && !artist.getAlbums().isEmpty()) {
            throw new IllegalStateException("Cannot delete artist with albums. Remove all albums first.");
        }
        
        artistRepository.deleteById(id);
    }
    
    public List<ArtistDto> searchArtistsByName(String name) {
        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(name);
        return artistMapper.toDtoList(artists);
    }
}
