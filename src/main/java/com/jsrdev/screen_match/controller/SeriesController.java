package com.jsrdev.screen_match.controller;

import com.jsrdev.screen_match.dto.EpisodeResponse;
import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.service.SeriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping()
    public List<SeriesResponse> getSeries() {
        return seriesService.getSeries();
    }

    @GetMapping("/top5")
    public List<SeriesResponse> getTopSeries() {
        return seriesService.getTopSeries();
    }

    @GetMapping("/releases")
    public List<SeriesResponse> getLatestReleasesSeries() {
        return seriesService.getLatestReleasesSeries();
    }

    @GetMapping("/{id}")
    public SeriesResponse getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @GetMapping("/{id}/seasons/all")
    public List<EpisodeResponse> getAllSeasons(@PathVariable Long id) {
        return seriesService.getAllSeasons(id);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}")
    public List<EpisodeResponse> getEpisodesBySeasonNumber(@PathVariable Long id, @PathVariable Long seasonNumber) {
        return seriesService.getEpisodesBySeasonNumber(id, seasonNumber);
    }

    @GetMapping("/genre/{genre}")
    public List<SeriesResponse> getSeriesByGenre(@PathVariable String genre) {
        return seriesService.getSeriesByGenre(genre);
    }

    @GetMapping("/{id}/seasons/top")
    public List<EpisodeResponse> getTopEpisodes(@PathVariable Long id) {
        return seriesService.getTopEpisodes(id);
    }
}