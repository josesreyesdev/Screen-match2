package com.jsrdev.screen_match.main;

import com.jsrdev.screen_match.model.SeriesData;
import com.jsrdev.screen_match.service.ApiService;
import com.jsrdev.screen_match.service.ConvertData;
import com.jsrdev.screen_match.utils.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FirstMenu {
    private final ApiService apiService = new ApiService();
    //private final ConvertData convertData = new ConvertData();

    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        String apiKey = Configuration.API_KEY_OMDB;
        String seriesName = encodeAndFormatSeriesName("game of thrones");
        var myUrl = "https://www.omdbapi.com/?t="+ seriesName +"&apikey=" + apiKey;

        var fetchData = new ApiService();
        String json = fetchData.fetchData(myUrl);
        System.out.println("\nJson: " + json);

        SeriesData seriesData = new ConvertData().getData(json, SeriesData.class);
        System.out.println("\nSeries Data: " + seriesData);
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }
}
