package com.anthology.repository;

import com.anthology.model.DifficultyRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DifficultyRatingRepository extends JpaRepository<DifficultyRating, Long> {
}
