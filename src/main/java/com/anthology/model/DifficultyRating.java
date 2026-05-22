package com.anthology.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "difficulty_ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "song_version_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DifficultyRating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_version_id", nullable = false)
    private SongVersion songVersion;

    @Min(1)
    @Max(5)
    @Column(name = "difficulty_score", nullable = false)
    private Integer difficultyScore;
}
