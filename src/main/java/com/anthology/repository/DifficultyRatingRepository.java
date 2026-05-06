package com.anthology.repository;

import com.anthology.model.DifficultyRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyRatingRepository extends JpaRepository<DifficultyRating, Long> {
}
