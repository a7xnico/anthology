package com.anthology.repository;

import com.anthology.enums.Instrument;
import com.anthology.model.SongVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongVersionRepository extends JpaRepository<SongVersion, Long> {
    boolean existsBySongIdAndInstrument(Long id, Instrument instrument);

    List<SongVersion> findBySongId(Long songId);

    Optional<SongVersion> findBySongIdAndInstrument(Long songId, Instrument instrument);
}
