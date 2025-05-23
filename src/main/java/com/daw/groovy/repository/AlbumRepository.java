package com.daw.groovy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.groovy.entity.Album;
import com.daw.groovy.entity.Artist;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    
    List<Album> findByArtist(Artist artist);
    
    List<Album> findByArtistId(Long artistId);
}
