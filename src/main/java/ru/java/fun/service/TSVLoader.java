package ru.java.fun.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.java.fun.service.TSVSaver.*;

public class TSVLoader {

    private static final DateTimeFormatter[] TSV_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd.MM.yy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy"),
    };

    private static OffsetDateTime parseCsvData(String value, DateTimeFormatter format) {
        try {
            return LocalDate.parse(value, format)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toOffsetDateTime();
        } catch (DateTimeParseException dtfe) {
            return parseCsvData(value, 0);
        }
    }

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
        List<String> lines = reader.lines()
                .collect(Collectors.toList());
        String[] first = lines.get(0)
                .split("\t");
        var header = header(first);
        var format = premiereFormat(first);
        Map<Integer, List<Episode>> result = lines.stream()
                .skip(1)
                .map(line -> episode(header, format, line))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return result.entrySet()
                .stream()
                .map(kv -> new Season(kv.getKey(), kv.getValue()))
                .collect(Collectors.toList());
    }

    private static DateTimeFormatter premiereFormat(String[] line) {
        return Arrays.stream(line)
                .filter(value -> StringUtils.startsWithIgnoreCase(value, PREMIERE + " "))
                .map(s -> StringUtils.substringAfter(s, " "))
                .findFirst()
                .map(DateTimeFormatter::ofPattern)
                .orElse(null);
    }

    private static String cutPremiere(String value) {
        if(StringUtils.startsWithIgnoreCase(value, PREMIERE)) {
            return PREMIERE;
        }
        return value;
    }

    private static Map<String, Integer> header(String[] line) {
        AtomicInteger counter = new AtomicInteger(0);
        return Arrays.stream(line)
                .map(StringUtils::trim)
                .map(TSVLoader::cutPremiere)
                .collect(Collectors.toMap(
                        s -> s,
                        s -> counter.getAndIncrement(),
                        (s1, s2) -> {
                            throw new IllegalArgumentException("Not unique column name: " + s1);
                        },
                        () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
                ));
    }

    private static Optional<Pair<Integer, Episode>> episode(
            Map<String, Integer> header,
            DateTimeFormatter premiereFormat,
            String line
    ) {
        line = line.trim();
        if (!line.isEmpty()) {
            Integer indexId = header.get(EPISODE_ID);
            Integer indexName = header.get(EPISODE_NAME);
            Integer indexNameOriginal = header.get(EPISODE_NAME_ORIGINAL);
            Integer indexPremiere = header.get(PREMIERE);
            Integer indexDescription = header.get(DESCRIPTION);
            String[] columns = line.split("\t");
            return EpisodeIdDetector.detect(columns[indexId])
                    .map(id -> {
                        String title = columns[indexName];
                        String nameOriginal = null;
                        if (indexNameOriginal != null) {
                            nameOriginal = columns[indexNameOriginal];
                        }
                        String premiere = columns[indexPremiere];
                        String description = columns[indexDescription];
                        return Pair.of(
                                id.getSeason(),
                                new Episode(
                                        id.getEpisode(),
                                        title,
                                        nameOriginal,
                                        description,
                                        parseCsvData(premiere, premiereFormat)
                                )
                        );
                    });
        }
        return Optional.empty();
    }

}
