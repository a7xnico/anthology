package com.anthology.repository;

import com.anthology.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findBySongVersionId(Long songVersionId);

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.user
            JOIN FETCH c.songVersion sv
            WHERE sv.deletedAt IS NULL
            """)
    List<Comment> findAllWithActiveSongVersion();
}
