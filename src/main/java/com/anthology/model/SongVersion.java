package com.anthology.model;

import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "song_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongVersion extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Instrument instrument;

    @Column(name = "difficulty_avg", precision = 3, scale = 2)
    private BigDecimal difficultyAvg = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.APPROVED;

    @NotBlank
    @Column(name = "pdf_drive_id", nullable = false, length = 100)
    private String pdfDriveId;

    @NotBlank
    @Column(name = "pdf_url", nullable = false, length = 500)
    private String pdfUrl;
}
