package com.anthology.repository;

import com.anthology.model.ArtistRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {
}
