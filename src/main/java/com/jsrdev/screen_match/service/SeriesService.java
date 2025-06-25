package com.jsrdev.screen_match.service;

import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.mappers.SeriesMapper;
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
        return seriesRepository.findAll().stream()
                .map(s -> new SeriesMapper().mapToSeriesResponse(s))
                .collect(Collectors.toList());
    }
}
