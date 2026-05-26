package com.anthology.repository;

import com.anthology.enums.SongSuggestionStatus;
import com.anthology.model.SongSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongSuggestionRepository extends JpaRepository<SongSuggestion, Long> {
    boolean exitsByIdUserAndArtist(String title,String artistName);
    Optional<SongSuggestion>findByIdAndStatus(Long id, SongSuggestionStatus status);
}
