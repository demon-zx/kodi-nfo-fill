package ru.java.fun.command;

import ru.java.fun.ExecutionException;
import ru.java.fun.Input;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.kinopoisk.dev.Document;
import ru.java.fun.kinopoisk.dev.Movie;
import ru.java.fun.kinopoisk.dev.SearchResult;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.ThumbNfo;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;

public class FilmCommand implements Command {
    @Override
    public void execute(Input input) throws IOException {
        Api api = new Api(input.getUri(), input.getToken(), null, Duration.ofSeconds(15), Duration.ofSeconds(15));
        String query = Objects.requireNonNullElse(input.getFilmName(), input.getFileName());
        SearchResult search = api.search(query, 1, 1);
        Document first = search.getDocs()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ExecutionException("Not found first."));
        System.out.printf("Found: %s, %s.%n", first.getName(), first.getYear());
        Movie movie = api.findMovieById(first.getId());
        MovieNfo nfo = NfoGenerator.movie(movie);
        NfoSaver.save(input, nfo);
        for (ThumbNfo thumb : nfo.getThumbs()) {
            URI uri = URI.create(thumb.getPreview());
            ThumbNfo.Aspect aspect = thumb.getAspect();
            Path path = Paths.get(input.getFileName() + "-" + aspect.name() + ".jpg");
            api.saveImage(path, uri);
        }

    }
}
