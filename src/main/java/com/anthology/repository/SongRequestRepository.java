package com.anthology.repository;

import com.anthology.model.SongRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRequestRepository extends JpaRepository<SongRequest, Long> {
}
