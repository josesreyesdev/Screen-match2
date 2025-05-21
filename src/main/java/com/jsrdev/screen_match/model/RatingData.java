package com.jsrdev.screen_match.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// classification
@JsonIgnoreProperties(ignoreUnknown = true)
public record RatingData(
        @JsonAlias("Source") String source,
        @JsonAlias("Value") String value
) { }
