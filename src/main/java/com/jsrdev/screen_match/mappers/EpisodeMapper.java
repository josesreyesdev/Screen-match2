package com.jsrdev.screen_match.mappers;

import com.jsrdev.screen_match.model.Episode;
import com.jsrdev.screen_match.model.EpisodeData;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class EpisodeMapper {
    public Episode mapToEpisode(String seasonStr, EpisodeData e) {
        var season = Optional.ofNullable(seasonStr)
                .map(Integer::valueOf)
                .orElse(-1);

        double evaluation = Optional.ofNullable(e.evaluation())
                .filter(val -> !val.equalsIgnoreCase("N/A"))
                .map(val -> {
                    try {
                        return Double.valueOf(val);
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                })
                .orElse(0.0);

        LocalDate releaseDate = Optional.ofNullable(e.released())
                .map(date -> {
                    try {
                        return LocalDate.parse(date);
                    } catch (DateTimeParseException ex) {
                        return null;
                    }
                })
                .orElse(null);

        return new Episode(season, e.title(), e.episode(), evaluation, releaseDate);
    }
}
