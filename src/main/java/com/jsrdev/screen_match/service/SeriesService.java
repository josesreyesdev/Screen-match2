package com.jsrdev.screen_match.service;

import com.jsrdev.screen_match.dto.EpisodeResponse;
import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.mappers.EpisodeMapper;
import com.jsrdev.screen_match.mappers.SeriesMapper;
import com.jsrdev.screen_match.model.Episode;
import com.jsrdev.screen_match.model.Genre;
import com.jsrdev.screen_match.model.Series;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public List<SeriesResponse> getSeries() {
        return seriesResponses(seriesRepository.findAll());
    }

    public List<SeriesResponse> getTopSeries() {
        return seriesResponses(seriesRepository.findTop5ByOrderByEvaluationDesc());
    }

    public List<SeriesResponse> getLatestReleasesSeries() {
        return seriesResponses(seriesRepository.latestReleasesSeries());
    }

    private List<SeriesResponse> seriesResponses(List<Series> series) {
        return series.stream()
                .map(s -> new SeriesMapper().mapToSeriesResponse(s))
                .collect(Collectors.toList());
    }

    private Optional<Series> findSeriesById(Long id) {
        return seriesRepository.findById(id);
    }

    public SeriesResponse getSeriesById(Long id) {
        return findSeriesById(id)
                .map(s -> new SeriesMapper().mapToSeriesResponse(s))
                .orElse(null);
    }

    public List<EpisodeResponse> getAllSeasons(Long id) {
        return findSeriesById(id)
                .map(s -> episodeResponses(s.getEpisodes()))
                .orElse(null);
    }

    private List<EpisodeResponse> episodeResponses(List<Episode> episodes) {
        return episodes.stream()
                .map(e -> new EpisodeMapper().mapToEpisodeResponse(e))
                .collect(Collectors.toList());
    }

    public List<EpisodeResponse> getEpisodesBySeasonNumber(Long id, Long seasonNumber) {
        return episodeResponses(seriesRepository.getEpisodesBySeasonNumber(id, seasonNumber));
    }

    public List<SeriesResponse> getSeriesByGenre(String genre) {
        Genre parsedGenre = Genre.parseGenres(genre);

        List<Series> seriesByGenre = seriesRepository.findByGenre(parsedGenre).stream()
                .flatMap(Optional::stream)
                .toList();

        return seriesByGenre.isEmpty() ? null : seriesResponses(seriesByGenre);
    }

    public List<EpisodeResponse> getTopEpisodes(Long id) {
        return findSeriesById(id)
                .map(s -> episodeResponses(seriesRepository.findTopEpisodes(s)))
                .orElse(null);
    }
}
