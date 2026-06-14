package com.anthology.repository;

import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.anthology.model.SongVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongVersionRepository extends JpaRepository<SongVersion, Long> {
    boolean existsBySongIdAndInstrument(Long id, Instrument instrument);

    List<SongVersion> findBySongId(Long songId);

    Optional<SongVersion> findBySongIdAndInstrument(Long songId, Instrument instrument);

    List<SongVersion> findBySongIdAndStatus(Long songId, Status status);

    List<SongVersion> findByStatus(Status status);

    @Query("""
    SELECT sv FROM SongVersion sv
    WHERE sv.song.artist.id = :artistId
    """)
    List<SongVersion> findByArtistId(@Param("artistId") Long artistId);

    @Query(value = "SELECT * FROM song_versions WHERE song_id = :songId AND deleted_at IS NOT NULL", nativeQuery = true)
    List<SongVersion> findDeletedBySongId(@Param("songId") Long songId);
}
