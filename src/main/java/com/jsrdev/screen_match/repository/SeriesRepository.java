package com.jsrdev.screen_match.repository;

import com.jsrdev.screen_match.model.Episode;
import com.jsrdev.screen_match.model.Genre;
import com.jsrdev.screen_match.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    /* Derived Queries */
    Optional<Series> findByTitleContainsIgnoreCase(String seriesTitle);

    List<Series> findTop5ByOrderByEvaluationDesc();

    List<Optional<Series>> findByGenre(Genre genre);

    //List<Series> findByTotalSeasonsLessThanEqualAndEvaluationGreaterThanEqual(int totalSeason, double evaluation);

    /* Native Queries => Statics queries, no recommended */
    /*@Query(value = "SELECT * FROM series WHERE series.total_seasons <= 6 AND series.evaluation >= 7.6", nativeQuery = true)
    List<Series> seriesByTotalSeasonsAndEvaluation(); */

    // JPQL => The Java Persistence Query Language
    @Query("SELECT s FROM Series s WHERE s.totalSeasons <= :totalSeasons AND s.evaluation >= :evaluation")
    List<Series> seriesByTotalSeasonsAndEvaluation(int totalSeasons, double evaluation);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE e.title ILIKE %:episodeTitle%")
    List<Optional<Episode>> findEpisodesTitle(String episodeTitle);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.evaluation DESC LIMIT :limit")
    List<Episode> findTopEpisodesBySeries(Series series, int limit);

    @Query("SELECT s FROM Series s JOIN s.episodes e GROUP BY s ORDER BY MAX(e.releaseDate) DESC LIMIT 5")
    List<Series> latestReleasesSeries();

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s.id = :id AND e.season = :seasonNumber")
    List<Episode> getEpisodesBySeasonNumber(Long id, Long seasonNumber);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.evaluation DESC LIMIT 5")
    List<Episode> findTopEpisodes(Series series);
}
