package com.jsrdev.screen_match.repository;

import com.jsrdev.screen_match.model.Genre;
import com.jsrdev.screen_match.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    // Derived queries
    Optional<Series> findByTitleContainsIgnoreCase(String seriesTitle);

    List<Series> findTop5ByOrderByEvaluationDesc();

    List<Optional<Series>> findByGenre(Genre genre);
}
