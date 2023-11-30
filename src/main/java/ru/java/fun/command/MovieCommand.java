package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.ExecutionException;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.kinopoisk.dev.Document;
import ru.java.fun.kinopoisk.dev.Movie;
import ru.java.fun.kinopoisk.dev.SearchResult;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.ThumbNfo;
import ru.java.fun.util.FileUtil;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "movie",
        description = "Scrap movie info"
)
public class MovieCommand extends AbstractCommand {

    @Option(
            names = {"-n", "--name"},
            description = "Movie name"
    )
    private String name;

    @Option(
            names = {"-f", "--file-name"},
            description = "File",
            required = true
    )
    private Path file;

    @Override
    public Integer call() throws Exception {
        Api api = new Api(uri, token, null, Duration.ofSeconds(15), Duration.ofSeconds(15));
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
        printf("Found: %s, %s.%n", first.getName(), first.getYear());
        Movie movie = api.findMovieById(first.getId());
        MovieNfo nfo = NfoGenerator.movie(movie);
        NfoSaver.save(file, nfo);
        for (ThumbNfo thumb : nfo.getThumbs()) {
            URI uri = URI.create(thumb.getPreview());
            ThumbNfo.Aspect aspect = thumb.getAspect();
            Path path = FileUtil.replaceExtension(file, "-" + aspect.name() + ".jpg");
            api.saveImage(path, uri);
        }
        return null;
    }

}
