package com.jsrdev.screen_match.main;

import com.jsrdev.screen_match.mappers.EpisodeMapper;
import com.jsrdev.screen_match.mappers.SeriesMapper;
import com.jsrdev.screen_match.model.*;
import com.jsrdev.screen_match.repository.SeriesRepository;
import com.jsrdev.screen_match.service.ApiService;
import com.jsrdev.screen_match.service.ConvertData;
import com.jsrdev.screen_match.utils.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SeriesMenu {

    private final ApiService apiService = new ApiService();
    private final ConvertData deserializeData = new ConvertData();

    private final Scanner scanner = new Scanner(System.in);

    private List<Series> series;

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
                    System.out.println("\n****** Execution Completed ******\n");
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
        showSearchedSeries();
        String seriesName = input("Enter the series name to show its episodes: ");
        System.out.println("\n🎬 Searching Episodes...");

        Optional<Series> searchSeries = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(seriesName.toLowerCase()))
                .findFirst();

        if (searchSeries.isEmpty()) {
            System.out.println("\nI did not find " + seriesName + " series available to show their episodes");
            return;
        }

        // Seasons
        List<SeasonData> seasons = new ArrayList<>();
        Series seriesFound = searchSeries.get();
        for (int i = 1; i <= seriesFound.getTotalSeasons(); i++) {
            String url = buildURL(encodedAndFormatSeriesName(seriesFound.getTitle()), String.valueOf(i), null);
            String json = apiService.fetchData(url);
            SeasonData seasonData = deserializeData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        //seasons.forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodeData().stream()
                        .map(e -> new EpisodeMapper().mapToEpisode(s.season(), e))
                )
                .collect(Collectors.toList());

        seriesFound.setEpisodes(episodes);
        seriesRepository.save(seriesFound);

        episodes.forEach(System.out::println);
    }

    private void showSearchedSeries() {
        System.out.println("\n📺 Showing Searched Series...");

        series = seriesRepository.findAll();

        if (series.isEmpty()) {
            System.out.println("\nI did not find any series available in DB");
            return;
        }

        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void searchSeriesByTitle() {
        String seriesName = input("Enter the name of the series to search: ");

        System.out.println("\n📝 Searching Series by Title...");

        Optional<Series> optionalSeries = seriesRepository.findByTitleContainsIgnoreCase(seriesName);

        if (optionalSeries.isEmpty()) {
            System.out.println("\nSeries " + seriesName + " not found");
            return;
        }

        System.out.println("\nSearched Series is: " + optionalSeries.get());
    }

    private void searchTop5Series() {
        System.out.println("\n🏆 Showing Top 5 Series...\n");
        List<Series> topSeries = seriesRepository.findTop5ByOrderByEvaluationDesc();
        for (int i = 0; i < topSeries.size(); i++) {
            Series s = topSeries.get(i);
            System.out.printf("%d.- %s (⭐ %.1f)\n", i + 1, s.getTitle(), s.getEvaluation());
        }
    }

    private void searchByGenreSeries() {
        String genderName = input("Enter the genre of the series to search: ");
        System.out.println("\n🎭 Searching Series by Genre...");

        Genre genre = Genre.parseGenres(genderName);

        List<Optional<Series>> opSeriesByGenre = seriesRepository.findByGenre(genre);

        List<Series> seriesByGenre = opSeriesByGenre.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (seriesByGenre.isEmpty()) {
            System.out.println("\nNo series found for genre: " + genre);
            return;
        }

        for (int i = 0; i < seriesByGenre.size(); i++) {
            Series s = seriesByGenre.get(i);
            System.out.printf(
                    "%d.- %s | Seasons: %d | Genre: %s | (⭐ %.1f)\n",
                    i + 1,
                    s.getTitle(),
                    s.getTotalSeasons(),
                    s.getGenre(),
                    s.getEvaluation()
            );
        }
    }

    private void filterSeriesBySeasonAndEvaluation() {
        System.out.print("\nFilter series with how many seasons?");
        int totalSeason = scanner.nextInt();
        scanner.nextLine();

        System.out.print("\nFrom which evaluation value?");
        double evaluation = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("\n📊 Filtering Series by Season and Evaluation...");

        List<Series> filterSeries = seriesRepository
                . findByTotalSeasonsLessThanEqualAndEvaluationGreaterThanEqual(totalSeason, evaluation);

        if (filterSeries.isEmpty()) {
            System.out.println("\nNo series found with " + totalSeason + " season(s) and evaluation ≥ " + evaluation);
            return;
        }

        for (int i = 0; i < filterSeries.size(); i++) {
            Series s = filterSeries.get(i);
            System.out.printf(
                    "%d.- %s | Seasons: %d | Genre: %s | (⭐ %.1f)\n",
                    i + 1,
                    s.getTitle(),
                    s.getTotalSeasons(),
                    s.getGenre(),
                    s.getEvaluation()
            );
        }


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
