package ru.java.fun.service;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeIdDetector {

    private static final Pattern pClassic = Pattern.compile(
            "S(?<season>\\d+).*?E(?<episode>\\d+)",
            Pattern.CASE_INSENSITIVE
    );

    private EpisodeIdDetector() {
    }

    public static Optional<EpisodeId> detect(String name) {
        return detect(pClassic, name);
    }

    public static Optional<EpisodeId> detect(Path file) {
        var fileName = file.getFileName();
        return detect(fileName.toString());
    }

    @SuppressWarnings("SameParameterValue")
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

}
