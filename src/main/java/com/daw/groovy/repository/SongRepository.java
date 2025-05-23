package com.daw.groovy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.groovy.entity.Album;
import com.daw.groovy.entity.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    List<Song> findByAlbum(Album album);
    
    List<Song> findByAlbumId(Long albumId);
    
    List<Song> findByTitleContainingIgnoreCase(String title);
}
