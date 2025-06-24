package com.jsrdev.screen_match.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeriesController {

    @GetMapping("/series")
    public String getString() {
        return "Hello World, it is my first message";
    }
}