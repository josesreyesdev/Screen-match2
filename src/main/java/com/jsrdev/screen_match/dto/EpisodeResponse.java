package com.jsrdev.screen_match.dto;

import java.time.LocalDate;

public record EpisodeResponse(
        Integer season,
        String title,
        Integer episodeNumber,
        Double evaluation,
        LocalDate releaseDate,
        Long seriesId
) {
}
