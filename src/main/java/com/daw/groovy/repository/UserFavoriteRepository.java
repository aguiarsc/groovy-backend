package com.daw.groovy.repository;

import com.daw.groovy.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    
    /**
     * Find all favorite songs for a specific user
     * @param userId the user's ID
     * @return list of favorites for the user
     */
    List<UserFavorite> findByUserId(Long userId);
    
    /**
     * Find a specific user-song favorite relationship
     * @param userId the user's ID
     * @param songId the song's ID
     * @return the favorite relationship if exists
     */
    Optional<UserFavorite> findByUserIdAndSongId(Long userId, Long songId);
    
    /**
     * Check if a song is favorited by a specific user
     * @param userId the user's ID
     * @param songId the song's ID
     * @return true if favorited, false otherwise
     */
    boolean existsByUserIdAndSongId(Long userId, Long songId);
    
    /**
     * Delete a favorite relationship
     * @param userId the user's ID
     * @param songId the song's ID
     */
    @Transactional
    void deleteByUserIdAndSongId(Long userId, Long songId);

    @Transactional
    void deleteBySongId(Long songId);
}
