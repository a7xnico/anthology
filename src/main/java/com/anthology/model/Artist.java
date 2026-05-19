package com.anthology.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Column(name = "stage_name", nullable = false)
    private String stageName;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private String instagram;

    private String spotify;

    private String youtube;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
