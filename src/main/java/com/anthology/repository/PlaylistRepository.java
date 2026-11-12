package com.anthology.repository;

import com.anthology.model.Playlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    boolean existsByUserIdAndName(Long userId, String name);
    boolean existsByUserIdAndIsDefaultTrue(Long userId);

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM playlist_songs
        WHERE song_version_id IN (SELECT id FROM song_versions WHERE song_id = :songId)
        """, nativeQuery = true)
    void removeSongVersionsBySongId(@Param("songId") Long songId);
}
