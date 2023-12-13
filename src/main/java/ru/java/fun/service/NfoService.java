package ru.java.fun.service;

import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.*;
import ru.java.fun.nfo.EpisodeNfo;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.TVShowNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
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

    public void fillSerial(Path directory, Set<String> extensions, String name) throws IOException {
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
        List<Season> seasons = api.findSeasonsById(serial.getId());
        TVShowNfo nfo = NfoMapper.tvShow(serial, seasons);
        Map<EpisodeId, Episode> episodes = new HashMap<>();
        for (Season season : seasons) {
            for (Episode episode : season.getEpisodes()) {
                episodes.put(new EpisodeId(season.getNumber(), episode.getNumber()), episode);
            }
        }
        List<Path> episodeFiles = findEpisodes(directory, extensions);
        NfoFiles.save(directory, nfo);
        saveThumbs(
                nfo.getThumbs(),
                (aspect, number) -> directory.resolve(aspect.name() + number + ".jpg")
        );
        for (Path episodeFile : episodeFiles) {
            EpisodeId episodeId = EpisodeIdDetector.detect(episodeFile);
            Episode episode = episodes.get(episodeId);
            if (episode != null) {
                EpisodeNfo episodeNfo = NfoMapper.episode(episode);
                NfoFiles.save(episodeFile, episodeNfo);
            }
        }
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
