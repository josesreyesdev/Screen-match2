package com.jsrdev.screen_match.dto;

import com.jsrdev.screen_match.model.Genre;

public record SeriesResponse(
        Long id,
        String title,
        Integer totalSeasons,
        Double evaluation,
        String poster,
        Genre genre,
        String actors,
        String synopsis
) {
}
