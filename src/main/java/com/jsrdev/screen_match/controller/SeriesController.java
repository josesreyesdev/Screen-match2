package com.jsrdev.screen_match.controller;

import com.jsrdev.screen_match.model.Series;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeriesController {

    private final SeriesRepository seriesRepository;

    public SeriesController(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    @GetMapping("/series")
    public List<Series> getSeries() {
        return seriesRepository.findAll();
    }
}