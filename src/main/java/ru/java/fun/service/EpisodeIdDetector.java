package ru.java.fun.service;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeIdDetector {

    private static final Pattern pClassic = Pattern.compile(
            "S(?<season>\\d+).*?E(?<episode>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern pCyrillic = Pattern.compile(
            "сезон.*?(?<season>\\d+).*?серия.*?(?<episode>\\d+)",
            Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE
    );

    private static final Pattern pNumber = Pattern.compile(
            "(?<number>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private EpisodeIdDetector() {
    }

    public static Optional<EpisodeId> detect(Path file) {
        String name = file.getFileName()
                .toString();
        return detect(pClassic, name)
                .or(() -> detect(pCyrillic, name))
                .or(() -> {
                    String directory = file.getParent()
                            .getFileName()
                            .toString();
                    return findNumber(directory).flatMap(season -> findNumber(name).map(episode -> new EpisodeId(
                            season,
                            episode
                    )));
                });
    }

    private static Optional<EpisodeId> detect(Pattern pattern, String name) {
        Matcher m = pattern.matcher(name);
        if (m.find()) {
            return Optional.of(
                    new EpisodeId(
                            Integer.parseInt(m.group("season")),
                            Integer.parseInt(m.group("episode"))
                    )
            );
        }
        return Optional.empty();
    }

    private static Optional<Integer> findNumber(String value) {
        Matcher m = pNumber.matcher(value);
        if (m.find()) {
            return Optional.of(Integer.parseInt(m.group("number")));
        }
        return Optional.empty();
    }
}
