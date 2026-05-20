package com.anthology.repository;

import com.anthology.model.SongSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongSuggestionRepository extends JpaRepository<SongSuggestion, Long> {
}
