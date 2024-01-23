package ru.java.fun.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeasonIdDetector {

    private static final Pattern pattern = Pattern.compile(
            "S(?<season>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private SeasonIdDetector() {
    }

    public static Optional<Integer> detect(String name) {
        Matcher m = pattern.matcher(name);
        if (m.find()) {
            return Optional.of(Integer.parseInt(m.group("season")));
        }
        return Optional.empty();
    }
}
