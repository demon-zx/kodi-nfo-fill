package ru.java.fun.service;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeIdDetector {

    private static final Pattern pClassic = Pattern.compile(
            "S(?<season>\\d+).*?E(?<episode>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern pNumber = Pattern.compile(
            "(?<number>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private EpisodeIdDetector() {

    }

    public static EpisodeId detect(Path file) {
        String name = file.getFileName()
                .toString();
        Matcher m = pClassic.matcher(name);
        if(m.find()) {
            return new EpisodeId(
                    Integer.parseInt(m.group("season")),
                    Integer.parseInt(m.group("episode"))
            );
        }
        String directory = file.getParent()
                .getFileName()
                .toString();
        Integer season = findNumber(directory);
        Integer episode = findNumber(name);
        if(episode!=null) {
            return new EpisodeId(
                    Objects.requireNonNullElse(season, 1),
                    episode
            );
        }
        return null;
    }

    private static Integer findNumber(String value) {
        Matcher m = pNumber.matcher(value);
        if(m.find()) {
            return Integer.parseInt(m.group("number"));
        }
        return null;
    }
}
