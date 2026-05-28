package com.anthology.repository;

import com.anthology.model.ArtistSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistSuggestionRepository extends JpaRepository<ArtistSuggestion, Long> {
}
