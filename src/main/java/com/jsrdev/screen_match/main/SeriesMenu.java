package com.jsrdev.screen_match.main;

import com.jsrdev.screen_match.mappers.SeriesMapper;
import com.jsrdev.screen_match.model.SeasonData;
import com.jsrdev.screen_match.model.Series;
import com.jsrdev.screen_match.model.SeriesData;
import com.jsrdev.screen_match.repository.SeriesRepository;
import com.jsrdev.screen_match.service.ApiService;
import com.jsrdev.screen_match.service.ConvertData;
import com.jsrdev.screen_match.utils.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SeriesMenu {

    private final ApiService apiService = new ApiService();
    private final ConvertData deserializeData = new ConvertData();

    private final Scanner scanner = new Scanner(System.in);

    private final List<SeriesData> seriesDataList = new ArrayList<>();

    private final SeriesRepository seriesRepository;

    public SeriesMenu(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public void showMenu() {
        while (true) {
            Integer option = getValidOption();

            switch (option) {
                case 1 -> searchWebSeries();
                case 2 -> searchEpisodes();
                case 3 -> showSearchedSeries();
                case 4 -> searchSeriesByTitle();
                case 5 -> searchTop5Series();
                case 6 -> searchByGenreSeries();
                case 7 -> filterSeriesBySeasonAndEvaluation();
                case 8 -> searchEpisodeByTitle();
                case 9 -> searchTop5EpisodesBySeries();
                case 0 -> {
                    System.out.println("\n****** Execution Completed ******");
                    return;
                }
                default -> System.out.println("Option is not valid");
            }
        }
    }

    private Integer getValidOption() {
        Integer option = getEntryOption();

        while (option == null || option < 0 || option > 9) {
            option = invalidEntry();
        }

        return option;
    }

    private Integer getEntryOption() {
        String menu = """
                \n-----------------------------
                1.- Search New Web Series
                2.- Search Episodes
                3.- Show Searched Series
                4.- Search Series By Title
                5.- Top 5 Series
                6.- Search Series By Genre
                7.- Filter Series By Season And Evaluation
                8.- Search Episodes By Title
                9.- Top 5 Episodes By Series
                
                0.- Exit
                -----------------------------
                """;

        System.out.println(menu);
        System.out.print("Enter your option: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer invalidEntry() {
        System.out.println("❌ Invalid entry, try again!");
        return getEntryOption();
    }

    private void searchWebSeries() {
        SeriesData seriesData = getSeriesData();
        Series series = new SeriesMapper().mapToSeries(seriesData);
        seriesRepository.save(series);
        //seriesDataList.add(seriesData);

        System.out.println("\n\uD83D\uDCFA Searching Series...");
        System.out.println(seriesData);
    }

    private String encodedAndFormatSeriesName(String seriesName) {
        String encodedSeriesName = URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        return encodedSeriesName.replace("+", "%20");
    }

    private SeriesData getSeriesData() {
        String inputSeriesName = input("Enter series name: ").toLowerCase();
        String formattedName = encodedAndFormatSeriesName(inputSeriesName);

        String url = buildURL(formattedName, null, null);
        String json = apiService.fetchData(url);
        return deserializeData.getData(json, SeriesData.class);
    }

    private String input(String message) {
        System.out.print("\n" + message);
        String result;
        do {
            result = scanner.nextLine().trim();
        } while (result.isEmpty());
        return result;
    }

    private void searchEpisodes() {
        System.out.println("\n🎬 Searching Episodes...");

        if (seriesDataList.isEmpty()) {
            System.out.println("\nI did not find any series available to show their episodes");
            return;
        }

        // Seasons
        List<SeasonData> seasons = new ArrayList<>();
        int totalSeason = Integer.parseInt(seriesDataList.getLast().totalSeasons());
        for (int i = 1; i <= totalSeason; i++) {
            String url = buildURL(encodedAndFormatSeriesName(seriesDataList.getLast().title()), String.valueOf(i), null);
            String json = apiService.fetchData(url);
            SeasonData seasonData = deserializeData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);
    }

    private void showSearchedSeries() {
        System.out.println("\n📺 Showing Searched Series...");

        List<Series> series = seriesRepository.findAll();

        if (series.isEmpty()) {
            System.out.println("\nI did not find any series available in DB");
            return;
        }

        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void searchSeriesByTitle() {
        System.out.println("\n📝 Searching Series by Title...");
    }

    private void searchTop5Series() {
        System.out.println("\n🏆 Showing Top 5 Series...");
    }

    private void searchByGenreSeries() {
        System.out.println("\n🎭 Searching Series by Genre...");
    }

    private void filterSeriesBySeasonAndEvaluation() {
        System.out.println("\n📊 Filtering Series by Season and Evaluation...");
    }

    private void searchEpisodeByTitle() {
        System.out.println("\n🔎 Searching Episode by Title...");
    }

    private void searchTop5EpisodesBySeries() {
        System.out.println("\n🏅 Showing Top 5 Episodes by Series...");
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
