package com.anthology.repository;

import com.anthology.enums.Status;
import com.anthology.model.ArtistSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistSuggestionRepository extends JpaRepository<ArtistSuggestion, Long> {

    List<ArtistSuggestion> findByStatus(Status status);
}
