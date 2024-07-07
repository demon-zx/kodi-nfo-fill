package ru.java.fun.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSVSaver {

    public static final DateTimeFormatter DTF_STANDARD = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public static final String EPISODE_ID = "Episode id";
    public static final String EPISODE_NAME = "Episode name";
    public static final String EPISODE_NAME_ORIGINAL = "Original episode name";
    public static final String PREMIERE = "Premiere";
    public static final String DESCRIPTION = "Description";
    private static final List<Map.Entry<String, Function<Episode, String>>> columns = List.of(
            Map.entry(EPISODE_NAME, Episode::getTitle),
            Map.entry(EPISODE_NAME_ORIGINAL, Episode::getOriginalTitle),
            Map.entry(PREMIERE + " yyyy.MM.dd", e -> format(e.getPremiere())),
            Map.entry(DESCRIPTION, Episode::getDescription)
    );

    private static String format(OffsetDateTime value) {
        return Optional.ofNullable(value)
                .map(DTF_STANDARD::format)
                .orElse(null);
    }

    public static void save(PrintWriter writer, List<Season> seasons) throws IOException {
        Set<String> used = new HashSet<>();
        for (Season season : seasons) {
            for (Episode episode : season.getEpisodes()) {
                for (Map.Entry<String, Function<Episode, String>> e : columns) {
                    var getter = e.getValue();
                    String value = getter.apply(episode);
                    if (StringUtils.isNotBlank(value)) {
                        used.add(e.getKey());
                    }
                }
            }
        }
        List<Map.Entry<String, Function<Episode, String>>> usedColumns = columns.stream()
                .filter(c -> used.contains(c.getKey()))
                .collect(Collectors.toList());
        String header = Stream.concat(
                        Stream.of(EPISODE_ID),
                        usedColumns.stream()
                                .map(Map.Entry::getKey)
                )
                .collect(Collectors.joining("\t"));
        writer.println(header);
        for (Season season : seasons) {
            int seasonNumber = season.getNumber();
            for (Episode episode : season.getEpisodes()) {
                String id = "s" + seasonNumber + "e" + episode.getNumber();
                String line = Stream.concat(
                                Stream.of(id),
                                usedColumns.stream()
                                        .map(Map.Entry::getValue)
                                        .map(e -> e.apply(episode))
                                        .map(StringUtils::defaultString)
                                        .map(StringEscapeUtils::escapeCsv)
                        )
                        .collect(Collectors.joining("\t"));
                writer.println(line);
            }
        }
        writer.flush();
    }

}
