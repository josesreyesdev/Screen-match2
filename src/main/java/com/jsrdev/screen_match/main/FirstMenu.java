package com.jsrdev.screen_match.main;

import com.jsrdev.screen_match.model.Episode;
import com.jsrdev.screen_match.model.EpisodeData;
import com.jsrdev.screen_match.model.SeasonData;
import com.jsrdev.screen_match.model.SeriesData;
import com.jsrdev.screen_match.service.ApiService;
import com.jsrdev.screen_match.service.ConvertData;
import com.jsrdev.screen_match.utils.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
                System.out.println("\nSeason " + i);
                for (EpisodeData episodeData : episodesBySeason) {
                    System.out.println("Episode " + episodeData.episode() + ": " + episodeData.title());
                }
            }

            // Show episodes title by seasons with lambdas
            seasons.forEach(s -> {
                System.out.println("\nSeason: " + s.season());
                s.episodeData().forEach(e -> System.out.println("Episode " + e.episode() + ": " + e.title()));
            });

            // Convert all information to an episode data type list
            List<EpisodeData> episodeData = seasons.stream()
                    .flatMap(s -> s.episodeData().stream())
                    .collect(Collectors.toList()); // mutable list
                    //.toList(); // immutable list

            // top 5 episodes
            System.out.println("\nTop 5 episodes: ");
            episodeData.stream()
                    .filter(e -> !e.evaluation().equalsIgnoreCase("N/A"))
                    .sorted(Comparator.comparing(EpisodeData::evaluation).reversed())
                    .limit(5)
                    .forEach(System.out::println);

            // Convert to an episode type
            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodeData().stream()
                            .map(e -> new Episode(s.season(), e)))
                    .collect(Collectors.toList());

            System.out.println();
            episodes.forEach(System.out::println);

            // Search episodes by release date
            searchEpisodesByReleaseDate(episodes);
        }
    }

    private void searchEpisodesByReleaseDate(List<Episode> episodes) {
        Integer entryDate = getEntryDate();
        while (entryDate == null) {
            System.out.println("\nInvalid entry, please, try again!");
            entryDate = getEntryDate();
        }

        LocalDate searchByDate = LocalDate.of(entryDate, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchByDate))
                .forEach(e -> System.out.println(
                        "Season: " + e.getSeason() +
                                " Episode: " + e.getEpisodeNumber() +
                                " Title: " + e.getTitle() +
                                " Release Date: " + (e.getReleaseDate()).format(dtf)
                ));
    }

    private Integer getEntryDate() {
        System.out.println("\nEnter the date to search for episodes?: ");
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // clean invalid entry
            return null;
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
