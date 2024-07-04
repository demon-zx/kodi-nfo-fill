package ru.java.fun.service;

import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TSVLoader {

    private static final DateTimeFormatter[] TSV_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd.MM.yy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy"),
    };

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

    public static List<Season> load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        Map<Integer, List<Episode>> result = new HashMap<>();
        for (String line : lines) {
            line = line.trim();
            if(!line.isEmpty()) {
                String[] columns = line.split("\t");
                EpisodeIdDetector.detect(columns[0])
                        .ifPresent(id ->{
                            String title = columns[1];
                            String premiere = columns[2].replaceAll("\\D", ".");
                            String description = columns[3];
                            List<Episode> episodes = result.computeIfAbsent(
                                    id.getSeason(),
                                    sid -> new ArrayList<>()
                            );
                            episodes.add(new Episode(
                                    id.getEpisode(),
                                    title,
                                    title,
                                    description,
                                    parseCsvData(premiere, 0)
                            ));
                        });
            }
        }
        return result.entrySet()
                .stream()
                .map(kv -> new Season(kv.getKey(), kv.getValue()))
                .collect(Collectors.toList());
    }
}
