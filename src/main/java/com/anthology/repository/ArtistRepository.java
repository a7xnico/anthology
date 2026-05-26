package com.anthology.repository;

import com.anthology.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsByStageName(String stageName);

    Optional<Artist> findArtistById(Long id);
}
