package com.jsrdev.screen_match.controller;

import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.mappers.SeriesMapper;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SeriesController {

    private final SeriesRepository seriesRepository;

    public SeriesController(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @GetMapping("/series")
    public List<SeriesResponse> getSeries() {
        return seriesRepository.findAll().stream()
                .map(s -> new SeriesMapper().mapToSeriesResponse(s))
                .collect(Collectors.toList());
    }
}