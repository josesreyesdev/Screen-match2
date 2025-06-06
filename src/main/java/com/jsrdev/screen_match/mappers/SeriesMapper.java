package com.jsrdev.screen_match.mappers;

import com.jsrdev.screen_match.model.Genre;
import com.jsrdev.screen_match.model.Series;
import com.jsrdev.screen_match.model.SeriesData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class SeriesMapper {
    public Series mapToSeries(SeriesData seriesData) {

        int year = extractStartYear(seriesData.year());
        LocalDate released = parseToDate(seriesData.released());
        Genre genre = parseGenres(seriesData.genre());
        String synopsis = seriesData.plot(); //translateSynopsis(seriesData.plot());
        double evaluation = parseDoubleOrDefault(seriesData.imdbRating(), 0.0);
        int totalSeasons = parseIntOrDefault(seriesData.totalSeasons(), 0);

        return new Series(
                seriesData.title(),
                totalSeasons,
                evaluation,
                synopsis,
                genre,
                seriesData.actors(),
                synopsis
        );
    }

    private LocalDate parseToDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return LocalDate.MIN;
        }
    }

    private int extractStartYear(String yearStr) {
        if (yearStr == null || yearStr.trim().isEmpty()) return 0;
        try {
            String[] parts = yearStr.trim().split("–");
            return Integer.parseInt(parts[0].trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private Genre parseGenres(String genreStr) {
        String[] genres = genreStr.split(", ");
        for (String g : genres) {
            Genre genre = Genre.fromString(g.trim());
            if (genre != null) return genre;
        }
        for (String g : genres) {
            Genre genre = Genre.fromEsp(g.trim());
            if (genre != null) return genre;
        }
        throw new IllegalArgumentException("No valid genre found: " + genreStr);
    }

    /*private String translateSynopsis(String plot) {
        try {
            return OpenAIService.getTranslation(plot);
        } catch (OpenAiHttpException e) {
            return plot;
        }
    }*/

    private double parseDoubleOrDefault(String value, double defaultVal) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private int parseIntOrDefault(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
