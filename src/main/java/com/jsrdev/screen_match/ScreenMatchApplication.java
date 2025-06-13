package com.jsrdev.screen_match;

import com.jsrdev.screen_match.main.SeriesMenu;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

    private final SeriesRepository seriesRepository;

    public ScreenMatchApplication(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApplication.class, args);
    }

    @Override
    public void run(String... args) {
        SeriesMenu menu = new SeriesMenu(seriesRepository);
        menu.showMenu();
    }
}
