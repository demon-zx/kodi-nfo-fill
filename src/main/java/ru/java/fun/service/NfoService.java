package ru.java.fun.service;

import org.apache.commons.lang3.StringUtils;
import ru.java.fun.ExecutionException;
import ru.java.fun.nfo.EpisodeNfo;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.TVShowNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.service.model.*;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;


public class NfoService {

    private final Logger log;
    private final DataService dataService;

    public NfoService(Logger log, DataService dataService) {
        this.log = log;
        this.dataService = dataService;
    }

    public Movie fakeSerial(String name) {
        return new Movie(
                "",
                name,
                0,
                name,
                Status.ENDED,
                "",
                "",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true
        );
    }

    Path getFileName(Path file) {
        return file.getName(file.getNameCount() - 1);
    }

    public void fillMovie(Path file, String name) throws IOException {
        Path fileName = getFileName(file);
        String query = Objects.requireNonNullElseGet(
                name,
                fileName::toString
        );
        var search = dataService.findByQuery(query, 1, 1);
        var first = search.getData()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Not found."));
        log.printf(Logger.Level.INFO, "For %s found: %s, %s.%n", fileName, first.getName(), first.getYear());
        var movie = dataService.findMovieById(first.getId());
        MovieNfo nfo = NfoMapper.movie(movie);
        NfoFiles.save(file, nfo);
        saveThumbs(
                nfo.getThumbs(),
                (aspect, number) -> FileUtil.replaceExtension(file, "-" + aspect.name() + number + ".jpg")
        );
    }

    public Movie findSerial(
            Path directory,
            String name
    ) throws IOException {
        Path fileName = getFileName(directory);
        String query = Objects.requireNonNullElseGet(
                name,
                fileName::toString
        );
        var search = dataService.findByQuery(query, 1, 10);
        var serial = search.getData()
                .stream()
                .filter(MovieBase::isSerial)
                .findFirst()
                .orElse(null);
        if (serial != null) {
            log.printf(Logger.Level.INFO, "For %s found: %s, %s.%n", fileName, serial.getName(), serial.getYear());
            return dataService.findMovieById(serial.getId());
        }
        return null;
    }

    public void fillSerial(
            Path directory,
            Set<String> extensions,
            Movie serial,
            List<Season> seasons,
            Set<Integer> lockedSeasons
    ) throws IOException {
        TVShowNfo nfo = NfoMapper.tvShow(serial, seasons);
        Map<EpisodeId, Episode> episodes = new HashMap<>();
        for (var season : seasons) {
            for (var episode : season.getEpisodes()) {
                episodes.put(new EpisodeId(season.getNumber(), episode.getNumber()), episode);
            }
        }
        List<Path> episodeFiles = findEpisodes(directory, extensions);
        NfoFiles.save(directory, nfo);
        saveThumbs(
                nfo.getThumbs(),
                (aspect, number) -> directory.resolve(aspect.name() + number + ".jpg")
        );
        int foundCount = 0;
        int notDetectCount = 0;
        int notFoundCount = 0;
        int locked = 0;
        for (Path episodeFile : episodeFiles) {
            EpisodeId episodeId = EpisodeIdDetector.detect(episodeFile)
                    .orElse(null);
            ru.java.fun.service.model.Episode episode = episodes.get(episodeId);
            if (episodeId != null) {
                int seasonId = episodeId.getSeason();
                if (lockedSeasons.contains(seasonId)) {
                    locked += locked(episodeId);
                } else {
                    if (episode != null) {
                        foundCount += found(episodeId, episodeFile, episode);
                    } else {
                        notFoundCount += notFound(episodeId, episodeFile);
                    }
                }
            } else {
                notDetectCount += notDetect(episodeFile);
            }
        }
        log.println(Logger.Level.INFO, "");
        log.println(Logger.Level.INFO, "Summary:");
        log.printf(Logger.Level.INFO, "Found:      %4d%n", foundCount);
        log.printf(Logger.Level.INFO, "Not found:  %4d%n", notFoundCount);
        log.printf(Logger.Level.INFO, "Not detect: %4d%n", notDetectCount);
        log.printf(Logger.Level.INFO, "Locked: %4d%n", locked);
    }

    private int found(EpisodeId episodeId, Path episodeFile, ru.java.fun.service.model.Episode episode) {
        log.printf(
                Logger.Level.INFO,
                "Episode S%02dE%02d found for %s%n",
                episodeId.getSeason(),
                episodeId.getEpisode(),
                episodeFile.getFileName()
        );
        EpisodeNfo episodeNfo = NfoMapper.episode(episode);
        NfoFiles.save(episodeFile, episodeNfo);
        return 1;
    }

    private int locked(EpisodeId episodeId) {
        log.printf(
                Logger.Level.INFO,
                "Episode S%02dE%02d locked for write%n",
                episodeId.getSeason(),
                episodeId.getEpisode()
        );
        return 1;
    }

    private int notFound(EpisodeId episodeId, Path episodeFile) {
        log.printf(
                Logger.Level.INFO,
                "Episode S%01dE%02d NOT found for %s%n",
                episodeId.getSeason(),
                episodeId.getEpisode(),
                episodeFile.getFileName()
        );
        return 1;
    }

    private int notDetect(Path episodeFile) {
        log.printf(Logger.Level.INFO, "Episode NOT DETECT for %s%n", episodeFile.getFileName());
        return 1;
    }

    public Set<Integer> readSeasonLocks(Path directory) throws IOException {
        Path lockFile = directory.resolve("lock.txt");
        if (Files.exists(lockFile)) {
            return Files.readAllLines(lockFile, StandardCharsets.UTF_8)
                    .stream()
                    .map(StringUtils::trimToNull)
                    .filter(Objects::nonNull)
                    .filter(l -> !StringUtils.startsWith(l, "#"))
                    .map(SeasonIdDetector::detect)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    private List<Path> findEpisodes(Path directory, Set<String> extensions) throws IOException {
        //noinspection resource
        return Files.walk(directory, 2)
                .filter(p -> extensions.contains(FileUtil.extractExtension(p)))
                .collect(Collectors.toList());
    }

    private void saveThumbs(
            List<ThumbNfo> thumbs,
            BiFunction<ThumbNfo.Aspect, String, Path> fileFunction
    ) throws IOException {
        Map<ThumbNfo.Aspect, Integer> numbers = new HashMap<>();
        for (ThumbNfo thumb : thumbs) {
            if (thumb.getPreview() != null) {
                URI uri = URI.create(thumb.getPreview());
                ThumbNfo.Aspect aspect = thumb.getAspect();
                int number = numbers.compute(aspect, (s, c) -> requireNonNullElse(c, 0) + 1) - 1;
                Path path = fileFunction.apply(aspect, number == 0 ? "" : String.valueOf(number));
                dataService.saveImage(path, uri);
            }
        }
    }

    public void fakeSerial(
            Path directory,
            Set<String> extensions,
            String name,
            String episodePrefix
    ) throws IOException {
        Path fileName = getFileName(directory);
        name = Objects.requireNonNullElseGet(
                name,
                fileName::toString
        );
        var serial = fakeSerial(name);
        List<Path> episodeFiles = findEpisodes(directory, extensions);
        List<Episode> episodes = new ArrayList<>();
        int foundCount = 0;
        int notDetectCount = 0;
        int episodeNumber = 1;
        for (Path episodeFile : episodeFiles) {
            Episode episode = new Episode(
                    episodeNumber,
                    episodePrefix + " " + episodeNumber,
                    null,
                    null,
                    OffsetDateTime.MIN
            );
            episodes.add(episode);
            EpisodeId episodeId = EpisodeIdDetector.detect(episodeFile)
                    .orElse(null);
            episodeNumber++;
            if (episodeId != null) {
                foundCount += found(episodeId, episodeFile, episode);
            } else {
                notDetectCount++;
            }
        }
        Season season = new Season(1, episodes);
        TVShowNfo nfo = NfoMapper.tvShow(serial, List.of(season));
        NfoFiles.save(directory, nfo);
        log.println(Logger.Level.INFO, "");
        log.println(Logger.Level.INFO, "Summary:");
        log.printf(Logger.Level.INFO, "Found:      %4d%n", foundCount);
        log.printf(Logger.Level.INFO, "Not detect: %4d%n", notDetectCount);
    }
}
