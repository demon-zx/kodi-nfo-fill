package ru.java.fun.service;

import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.*;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.TVShowNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        saveThumbs(nfo.getThumbs(), aspect ->  FileUtil.replaceExtension(file, "-" + aspect.name() + ".jpg"));
    }

    public void fillSerial(Path file, String name) throws IOException {
        Path fileName = getFileName(file);
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
        Movie serial = api.findMovieById(first.getId());
        List<Season> seasons = api.findSeasonsById(serial.getId());
        TVShowNfo nfo = NfoMapper.tvShow(serial, seasons);
        NfoFiles.save(file, nfo);
        Map<ThumbNfo.Aspect, Integer> numbers = new HashMap<>();
        saveThumbs(
                nfo.getThumbs(),
                aspect -> {
                    int number = numbers.compute(aspect, (s, c) -> requireNonNullElse(c, 0) + 1) - 1;
                    return file.resolve(aspect.name() + (number == 0 ? "" : String.valueOf(number)) + ".jpg");
                }
        );
        for (ThumbNfo thumb : nfo.getThumbs()) {
            if (thumb.getPreview() != null) {
                URI uri = URI.create(thumb.getPreview());
                ThumbNfo.Aspect aspect = thumb.getAspect();
                Path path = FileUtil.replaceExtension(file, "-" + aspect.name() + ".jpg");
                api.saveImage(path, uri);
            }
        }
        System.out.println(seasons);
    }

    private void saveThumbs(List<ThumbNfo> thumbs, Function<ThumbNfo.Aspect, Path> fileFunction) throws IOException {
        for (ThumbNfo thumb : thumbs) {
            if (thumb.getPreview() != null) {
                URI uri = URI.create(thumb.getPreview());
                Path path = fileFunction.apply(thumb.getAspect());
                api.saveImage(path, uri);
            }
        }
    }
}
