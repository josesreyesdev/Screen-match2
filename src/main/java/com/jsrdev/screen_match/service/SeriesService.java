package com.jsrdev.screen_match.service;

import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.mappers.SeriesMapper;
import com.jsrdev.screen_match.model.Series;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
