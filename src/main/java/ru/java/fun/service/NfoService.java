package ru.java.fun.service;

import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.*;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


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
        for (ThumbNfo thumb : nfo.getThumbs()) {
            if (thumb.getPreview() != null) {
                URI uri = URI.create(thumb.getPreview());
                ThumbNfo.Aspect aspect = thumb.getAspect();
                Path path = FileUtil.replaceExtension(file, "-" + aspect.name() + ".jpg");
                api.saveImage(path, uri);
            }
        }
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
        System.out.println(seasons);
    }
}
