package com.anthology.repository;

import com.anthology.enums.Status;
import com.anthology.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    boolean existsByTitleAndArtistName(String title, String artistName);

    List<Album> findByArtistNameContainingIgnoreCase(String artistName);

    List<Album> findByArtistId(Long artistId);

    List<Album> findByStatus(Status status);
}
