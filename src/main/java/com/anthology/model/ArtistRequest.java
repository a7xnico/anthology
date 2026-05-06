package com.anthology.model;

import com.anthology.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequest extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name = "stage_name", nullable = false)
    private String stageName;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private String instagram;

    private String spotify;

    private String youtube;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
}
