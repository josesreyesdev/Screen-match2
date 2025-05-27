package com.jsrdev.screen_match.main;

import java.util.Arrays;
import java.util.List;

public class ExampleStreams {
    public void showStreams() {
        List<String> names = Arrays.asList("Brenda", "Luis", "Maria fernanda", "Erick", "Genesys", "Lupillo", "Lalo");

        names.stream()
                .sorted()
                .filter(n -> n.startsWith("L"))
                .limit(2)
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
