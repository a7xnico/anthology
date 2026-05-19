package com.anthology.repository;

import com.anthology.model.SongVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongVersionRepository extends JpaRepository<SongVersion, Long> {
}
