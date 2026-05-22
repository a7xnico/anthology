package com.anthology.repository;

import com.anthology.enums.Instrument;
import com.anthology.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByTitleIgnoreCaseAndArtistNameIgnoreCase(String title, String artistName);

    @Query("""
    SELECT DISTINCT s FROM Song s
    WHERE (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')))
    AND (:genre IS NULL OR LOWER(s.genre) = LOWER(:genre))
    AND (:artistName IS NULL OR LOWER(s.artistName) LIKE LOWER(CONCAT('%', :artistName, '%')))
    AND (:albumId IS NULL OR s.album.id = :albumId)
    """)
    List<Song> findByFilters(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("artistName") String artistName,
            @Param("albumId") Long albumId
    );

    @Query("""
    SELECT DISTINCT s FROM Song s
    JOIN s.songVersions sv
    WHERE sv.instrument = :instrument
    """)
    List<Song> findByInstrument(@Param("instrument") Instrument instrument);

}
