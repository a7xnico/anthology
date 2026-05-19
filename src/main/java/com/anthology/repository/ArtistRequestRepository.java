package com.anthology.repository;

import com.anthology.model.ArtistRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {
}
