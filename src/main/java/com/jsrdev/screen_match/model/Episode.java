package com.jsrdev.screen_match.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double evaluation;
    private LocalDate releaseDate;
    @ManyToOne @Setter
    private Series series;


    public Episode(Integer season, String title, Integer episodeNumber, Double evaluation, LocalDate releaseDate) {
        this.season = season;
        this.title = title;
        this.episodeNumber = episodeNumber;
        this.evaluation = evaluation;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title=" + title +
                ", episodeNumber=" + episodeNumber +
                ", evaluation=" + evaluation +
                ", releaseDate=" + releaseDate;
    }
}
