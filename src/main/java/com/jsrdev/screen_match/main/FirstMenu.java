package com.jsrdev.screen_match.main;

import com.jsrdev.screen_match.model.EpisodeData;
import com.jsrdev.screen_match.model.SeasonData;
import com.jsrdev.screen_match.model.SeriesData;
import com.jsrdev.screen_match.service.ApiService;
import com.jsrdev.screen_match.service.ConvertData;
import com.jsrdev.screen_match.utils.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FirstMenu {
    private final ApiService apiService = new ApiService();
    private final ConvertData convertData = new ConvertData();

    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.setProperty("https.protocols", "TLSv1.2");

        while (true) {
            String entrySeriesName = getEntrySeriesName();

            while (entrySeriesName.isBlank()) {
                System.out.println("Invalid entry, try again!");
                entrySeriesName = getEntrySeriesName();
            }
            entrySeriesName = entrySeriesName.trim().toLowerCase();

            if (entrySeriesName.equalsIgnoreCase("n")) {
                System.out.println("\nYour welcome!!");
                break;
            }

            String encodeAndFormatSeriesName = encodeAndFormatSeriesName(entrySeriesName);
            String url = buildURL(encodeAndFormatSeriesName, null, null);

            String json = apiService.fetchData(url);

            // Series
            SeriesData seriesData = convertData.getData(json, SeriesData.class);
            System.out.println("\nSeries Data: " + seriesData);

            // Seasons
            List<SeasonData> seasons = new ArrayList<>();
            for (int i = 1; i <= seriesData.totalSeasons(); i++) {
                url = buildURL(encodeAndFormatSeriesName, String.valueOf(i), null);
                json = apiService.fetchData(url);
                SeasonData seasonData = convertData.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }
            System.out.println();
            seasons.forEach(System.out::println);

            // Show episodes title by seasons
            for (int i = 0; i < seriesData.totalSeasons(); i++) {
                List<EpisodeData> episodesBySeason = seasons.get(i).episodeData();
                System.out.println("Season " + i);
                for (EpisodeData episodeData : episodesBySeason) {
                    System.out.println("Episode " + episodeData.episode() + ": " + episodeData.title());
                }
            }

            // Show episodes title by seasons with lambdas
            seasons.forEach(s -> {
                System.out.println("\nSeason: " + s.season());
                s.episodeData().forEach(e -> System.out.println("Episode " + e.episode() + ": " + e.title()));
            });
        }
    }

    private String getEntrySeriesName() {
        System.out.println("\nWrite series name to search or N to exit");
        return scanner.nextLine();
    }

    private String encodeAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private String buildURL(String seriesName, String seasonNumber, String episodeNumber) {
        String urlBase = Configuration.URL_BASE; // "https://www.omdbapi.com/?t="
        String apiKey = Configuration.API_KEY_OMDB;
        StringBuilder urlBuilder = new StringBuilder(urlBase);

        urlBuilder.append(seriesName).append("&apikey=").append(apiKey);
        if (seasonNumber != null && episodeNumber != null) {
            urlBuilder.append("&season=").append(seasonNumber).append("&episode=").append(episodeNumber);
        } else if (seasonNumber != null) {
            urlBuilder.append("&season=").append(seasonNumber);
        }
        return urlBuilder.toString();
    }
}
