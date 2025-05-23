package com.daw.groovy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daw.groovy.entity.Artist;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT a FROM Artist a LEFT JOIN FETCH a.albums")
    List<Artist> findAllWithAlbums();

    @Query("SELECT DISTINCT a FROM Artist a LEFT JOIN FETCH a.songs")
    List<Artist> findAllWithSongs();
}
