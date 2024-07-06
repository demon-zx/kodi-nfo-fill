package ru.java.fun.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringEscapeUtils;
import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TSVLoader {

    public static final DateTimeFormatter DTF_STANDARD = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter[] TSV_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd.MM.yy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy"),
    };
    private static final List<Map.Entry<String, Function<Episode, String>>> columns = List.of(
            Map.entry("Title", Episode::getTitle),
            Map.entry("Title Original", Episode::getOriginalTitle),
            Map.entry("Premiere yyyy.MM.dd", e -> format(e.getPremiere())),
            Map.entry("Description", Episode::getDescription)
    );

    private static OffsetDateTime parseCsvData(String value, int format) {
        try {
            return LocalDate.parse(value, TSV_FORMATTERS[format])
                    .atStartOfDay(ZoneId.systemDefault())
                    .toOffsetDateTime();
        } catch (DateTimeParseException dtfe) {
            if (format++ < TSV_FORMATTERS.length - 1) {
                return parseCsvData(value, format);
            }
        }
        return null;
    }

    public static List<Season> load(BufferedReader reader) throws IOException {
        Map<Integer, List<Episode>> result = reader.lines()
                .map(TSVLoader::episode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return result.entrySet()
                .stream()
                .map(kv -> new Season(kv.getKey(), kv.getValue()))
                .collect(Collectors.toList());
    }

    private static Optional<Pair<Integer, Episode>> episode(String line) {
        line = line.trim();
        if (!line.isEmpty()) {
            String[] columns = line.split("\t");
            return EpisodeIdDetector.detect(columns[0])
                    .map(id -> {
                        String title = columns[1];
                        String premiere = columns[2].replaceAll("\\D", ".");
                        String description = columns[3];
                        return Pair.of(
                                id.getSeason(),
                                new Episode(
                                        id.getEpisode(),
                                        title,
                                        title,
                                        description,
                                        parseCsvData(premiere, 0)
                                )
                        );
                    });
        }
        return Optional.empty();
    }

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
                        Stream.of("Id"),
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
                                        .map(StringEscapeUtils::escapeCsv)
                        )
                        .collect(Collectors.joining("\t"));
                writer.println(line);
            }
        }
        writer.flush();
    }

}
