package com.jsrdev.screen_match.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "series")
@Getter
public class Series {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer totalSeasons;
    private Double evaluation;
    private String poster;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String actors;
    private String synopsis;

    @OneToMany(mappedBy = "series")
    private List<Episode> episodes;

    public Series() {
    }

    public Series(String title, Integer totalSeasons, Double evaluation, String poster, Genre genre, String actors, String synopsis) {
        this.title = title;
        this.totalSeasons = totalSeasons;
        this.evaluation = evaluation;
        this.poster = poster;
        this.genre = genre;
        this.actors = actors;
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", genre=" + genre +
                ", totalSeasons=" + totalSeasons +
                ", evaluation=" + evaluation +
                ", poster='" + poster + '\'' +
                ", actors='" + actors + '\'' +
                ", synopsis='" + synopsis ;
    }
}
