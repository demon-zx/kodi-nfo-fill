package ru.java.fun.service;

import org.apache.commons.lang3.StringUtils;
import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.*;
import ru.java.fun.nfo.EpisodeNfo;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.TVShowNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;


public class NfoService {

    private final Logger log;
    private final Api api;

    public NfoService(Logger log, Api api) {
        this.log = log;
        this.api = api;
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
        Page<Document> search = api.search(query, 1, 1);
        Document first = search.getDocs()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Not found."));
        log.printf(Logger.Level.INFO, "For %s found: %s, %s.%n", fileName, first.getName(), first.getYear());
        Movie movie = api.findMovieById(first.getId());
        MovieNfo nfo = NfoMapper.movie(movie);
        NfoFiles.save(file, nfo);
        saveThumbs(
                nfo.getThumbs(),
                (aspect, number) -> FileUtil.replaceExtension(file, "-" + aspect.name() + number + ".jpg")
        );
    }

    public void fillSerial(
            Path directory,
            Set<String> extensions,
            String name,
            boolean crossNumbering
    ) throws IOException {
        Set<Integer> lockedSeasons = readSeasonLocks(directory);
        Path fileName = getFileName(directory);
        String query = Objects.requireNonNullElseGet(
                name,
                fileName::toString
        );
        Page<Document> search = api.search(query, 1, 10);
        Document first = search.getDocs()
                .stream()
                .filter(Document::isSerial)
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Not found."));
        log.printf(Logger.Level.INFO, "For %s found: %s, %s.%n", fileName, first.getName(), first.getYear());
        Movie serial = api.findMovieById(first.getId());
        List<Season> seasons = api.findSeasonsById(serial.getId())
                .stream()
                .filter(s -> s.getNumber() > 0)
                .sorted(Comparator.comparing(Season::getNumber))
                .collect(Collectors.toList());
        TVShowNfo nfo = NfoMapper.tvShow(serial, seasons);
        Map<EpisodeId, Episode> episodes = new HashMap<>();
        int episodeNumber = 1;
        for (Season season : seasons) {
            for (Episode episode : season.getEpisodes()) {
                if (crossNumbering) {
                    episodes.put(new EpisodeId(1, episodeNumber++), episode);
                } else {
                    episodes.put(new EpisodeId(season.getNumber(), episode.getNumber()), episode);
                }
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
                    .map(eid -> crossNumbering ? new EpisodeId(1, eid.getEpisode()) : eid)
                    .orElse(null);
            Episode episode = episodes.get(episodeId);
            if (episodeId != null) {
                int seasonId = episodeId.getSeason();
                if(lockedSeasons.contains(seasonId)){
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

    private int found(EpisodeId episodeId, Path episodeFile, Episode episode) {
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
                "Episode S%02dE%02d NOT found for %s%n",
                episodeId.getEpisode(),
                episodeId.getEpisode(),
                episodeFile.getFileName()
        );
        return 1;
    }

    private int notDetect(Path episodeFile) {
        log.printf(Logger.Level.INFO, "Episode NOT DETECT for %s%n", episodeFile.getFileName());
        return 1;
    }


    private Set<Integer> readSeasonLocks(Path directory) throws IOException {
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


    private void saveThumbs(List<ThumbNfo> thumbs, BiFunction<ThumbNfo.Aspect, String, Path> fileFunction) throws IOException {
        Map<ThumbNfo.Aspect, Integer> numbers = new HashMap<>();
        for (ThumbNfo thumb : thumbs) {
            if (thumb.getPreview() != null) {
                URI uri = URI.create(thumb.getPreview());
                ThumbNfo.Aspect aspect = thumb.getAspect();
                int number = numbers.compute(aspect, (s, c) -> requireNonNullElse(c, 0) + 1) - 1;
                Path path = fileFunction.apply(aspect, number == 0 ? "" : String.valueOf(number));
                api.saveImage(path, uri);
            }
        }
    }
}
