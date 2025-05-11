package ru.java.fun.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static ru.java.fun.service.TSVSaver.DESCRIPTION;
import static ru.java.fun.service.TSVSaver.EPISODE_ID;
import static ru.java.fun.service.TSVSaver.EPISODE_NAME;
import static ru.java.fun.service.TSVSaver.EPISODE_NAME_ORIGINAL;
import static ru.java.fun.service.TSVSaver.PREMIERE;

public class TSVLoader {

    private static final DateTimeFormatter[] TSV_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd.MM.yy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy"),
    };

    private static OffsetDateTime parseDateTime(String value, DateTimeFormatter format) {
        return Optional.ofNullable(value)
                .filter(v -> format != null)
                .map(v -> LocalDate.parse(v, format))
                .map(v -> v.atStartOfDay(ZoneId.systemDefault()))
                .map(ZonedDateTime::toOffsetDateTime)
                .orElse(null);
    }

    private static OffsetDateTime parseDateTime(String value, int format) {
        try {
            return parseDateTime(value, TSV_FORMATTERS[format]);
        } catch (DateTimeParseException dtfe) {
            if (format++ < TSV_FORMATTERS.length - 1) {
                return parseDateTime(value, format);
            }
        }
        return null;
    }

    public static List<Season> load(Reader reader) throws IOException {
        CSVFormat csv = CSVFormat.Builder.create(CSVFormat.TDF)
                .setIgnoreEmptyLines(true)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        CSVParser parser = csv.parse(reader);
        var records = parser.getRecords();
        var premiereData = premiereData(parser.getHeaderMap());
        Map<Integer, List<Episode>> result = records.stream()
                .map(line -> episode(premiereData.getLeft(), premiereData.getRight(), line))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return result.entrySet()
                .stream()
                .map(kv -> new Season(kv.getKey(), kv.getValue()))
                .collect(Collectors.toList());
    }

    private static Pair<Integer, DateTimeFormatter> premiereData(Map<String, Integer> header) {
        return header.entrySet()
                .stream()
                .filter(value -> StringUtils.startsWithIgnoreCase(value.getKey(), PREMIERE + " "))
                .map(s -> 
                    Pair.of(s.getValue(), DateTimeFormatter.ofPattern(StringUtils.substringAfter(s.getKey(), " ")))
                )
                .findFirst()
                .orElse(null);
    }

    private static Optional<Pair<Integer, Episode>> episode(
            int premiereIndex,
            DateTimeFormatter premiereFormat,
            CSVRecord line
    ) {
        return EpisodeIdDetector.detect(line.get(EPISODE_ID))
                .map(id -> {
                    String title = line.get(EPISODE_NAME);
                    String nameOriginal = line.get(EPISODE_NAME_ORIGINAL);
                    String premiere = line.get(premiereIndex);
                    String description = line.get(DESCRIPTION);
                    OffsetDateTime premiereDateTime;
                    try {
                        premiereDateTime = parseDateTime(premiere, premiereFormat);
                    } catch (DateTimeParseException e) {
                        premiereDateTime = parseDateTime(premiere, 0);
                    }
                    return Pair.of(
                            id.getSeason(),
                            new Episode(
                                    id.getEpisode(),
                                    title,
                                    nameOriginal,
                                    description,
                                    premiereDateTime
                            )
                    );
                });
    }

}
