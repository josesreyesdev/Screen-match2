package com.jsrdev.screen_match.model;

public class Series {
    private String title;
    private Integer totalSeasons;
    private Double evaluation;
    private String poster;
    private Genre genre;
    private String actors;
    private String synopsis;

    public Series(String title, Integer totalSeasons, Double evaluation, String poster, Genre genre, String actors, String synopsis) {
        this.title = title;
        this.totalSeasons = totalSeasons;
        this.evaluation = evaluation;
        this.poster = poster;
        this.genre = genre;
        this.actors = actors;
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public String getPoster() {
        return poster;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getSynopsis() {
        return synopsis;
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
