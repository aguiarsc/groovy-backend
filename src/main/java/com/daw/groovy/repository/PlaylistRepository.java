package com.daw.groovy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.groovy.entity.Playlist;
import com.daw.groovy.entity.User;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    
    List<Playlist> findByUser(User user);
    
    List<Playlist> findByUserId(Long userId);
    
    List<Playlist> findByNameContainingIgnoreCase(String name);
}
