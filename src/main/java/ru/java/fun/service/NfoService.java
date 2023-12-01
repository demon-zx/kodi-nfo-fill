package ru.java.fun.service;

import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.kinopoisk.dev.Document;
import ru.java.fun.kinopoisk.dev.Movie;
import ru.java.fun.kinopoisk.dev.SearchResult;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;


public class NfoService {

    private final Logger log;
    private final Api api;

    public NfoService(Logger log, Api api) {
        this.log = log;
        this.api = api;
    }

    public void fill(Path file, String name) throws IOException {
        String query = Objects.requireNonNullElseGet(
                name,
                () -> file.getName(file.getNameCount() - 1)
                        .toString()
        );
        SearchResult search = api.search(query, 1, 1);
        Document first = search.getDocs()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Not found first."));
        log.printf("Found: %s, %s.%n", first.getName(), first.getYear());
        Movie movie = api.findMovieById(first.getId());
        MovieNfo nfo = NfoGenerator.movie(movie);
        NfoFiles.save(file, nfo);
        for (ThumbNfo thumb : nfo.getThumbs()) {
            URI uri = URI.create(thumb.getPreview());
            ThumbNfo.Aspect aspect = thumb.getAspect();
            Path path = FileUtil.replaceExtension(file, "-" + aspect.name() + ".jpg");
            api.saveImage(path, uri);
        }
    }
}
