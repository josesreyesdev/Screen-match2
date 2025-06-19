package com.jsrdev.screen_match.model;

import java.text.Normalizer;
import java.util.Arrays;

public enum Genre {
    ACTION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crimen"),
    SHORT("Short", "Corto"),
    HORROR("Horror", "Terror"),
    ADVENTURE("Adventure", "Aventura");

    private final String genreOmdb;
    private final String genreEsp;

    Genre(String genreOmdb, String genreEsp) {
        this.genreOmdb = genreOmdb;
        this.genreEsp = genreEsp;
    }

    public String getGenreOmdb() {
        return genreOmdb;
    }

    public String getGenreEsp() {
        return genreEsp;
    }

    public static Genre fromString(String text) {
        return Arrays.stream(values())
                .filter(g -> g.genreOmdb.equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

    public static Genre fromEsp(String text) {
        String normalizedText = normalize(text);
        return Arrays.stream(values())
                .filter(g -> normalize(g.genreEsp).equalsIgnoreCase(normalizedText))
                .findFirst()
                .orElse(null);
    }

    public static Genre parseGenres(String genre) {
        Genre found = fromEsp(genre);
        if (found != null) return found;

        found = fromString(genre);
        if (found != null) return found;

        throw new IllegalArgumentException("Genre: " + genre + " not found in enum class");
    }

    private static String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
