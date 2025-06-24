package com.jsrdev.screen_match;

import com.jsrdev.screen_match.main.SeriesMenu;
import com.jsrdev.screen_match.repository.SeriesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplicationMenu implements CommandLineRunner {

    private final SeriesRepository seriesRepository;

    public ScreenMatchApplicationMenu(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApplicationMenu.class, args);
    }

    @Override
    public void run(String... args) {
        SeriesMenu menu = new SeriesMenu(seriesRepository);
        menu.showMenu();
    }
}
