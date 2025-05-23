package com.daw.groovy.service;

import com.daw.groovy.dto.SongDto;
import com.daw.groovy.entity.Song;
import com.daw.groovy.entity.User;
import com.daw.groovy.entity.UserFavorite;
import com.daw.groovy.exception.ResourceNotFoundException;
import com.daw.groovy.mapper.SongMapper;
import com.daw.groovy.repository.SongRepository;
import com.daw.groovy.repository.UserFavoriteRepository;
import com.daw.groovy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    
    private final UserFavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    
    /**
     * Get all favorite songs for a user
     * 
     * @param userId the user's ID
     * @return list of song DTOs
     */
    public List<SongDto> getUserFavorites(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Get favorites and extract songs
        List<Song> favoriteSongs = favoriteRepository.findByUserId(userId).stream()
                .map(UserFavorite::getSong)
                .collect(Collectors.toList());
        
        return songMapper.toDtoList(favoriteSongs);
    }
    
    /**
     * Add a song to user's favorites
     * 
     * @param userId the user's ID
     * @param songId the song's ID
     * @return true if added, false if already exists
     */
    @Transactional
    public boolean addFavorite(Long userId, Long songId) {
        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            return false;
        }
        
        // Get user and song
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + songId));
        
        // Create and save new favorite
        UserFavorite favorite = UserFavorite.builder()
                .user(user)
                .song(song)
                .build();
        
        favoriteRepository.save(favorite);
        return true;
    }
    
    /**
     * Remove a song from user's favorites
     * 
     * @param userId the user's ID
     * @param songId the song's ID
     * @return true if removed, false if not found
     */
    @Transactional
    public boolean removeFavorite(Long userId, Long songId) {
        // Verify user and song exist
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        if (!songRepository.existsById(songId)) {
            throw new ResourceNotFoundException("Song not found with id: " + songId);
        }
        
        // Check if favorite exists
        if (!favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            return false;
        }
        
        favoriteRepository.deleteByUserIdAndSongId(userId, songId);
        return true;
    }
    
    /**
     * Check if a song is in user's favorites
     * 
     * @param userId the user's ID
     * @param songId the song's ID
     * @return true if favorited, false otherwise
     */
    public boolean isFavorite(Long userId, Long songId) {
        return favoriteRepository.existsByUserIdAndSongId(userId, songId);
    }
}
