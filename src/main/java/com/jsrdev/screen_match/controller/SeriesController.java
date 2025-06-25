package com.jsrdev.screen_match.controller;

import com.jsrdev.screen_match.dto.SeriesResponse;
import com.jsrdev.screen_match.service.SeriesService;
import org.springframework.web.bind.annotation.GetMapping;
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
}