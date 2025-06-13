package com.jsrdev.screen_match.repository;

import com.jsrdev.screen_match.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
