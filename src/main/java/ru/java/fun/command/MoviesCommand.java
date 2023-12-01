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

@CommandLine.Command(
        name = "movies",
        aliases = "mvs",
        mixinStandardHelpOptions = true,
        description = "Scrap movies info in directory"
)
public class MoviesCommand extends AbstractCommand {

    @Option(
            names = {"-d", "--directory"},
            description = "Directory with movies",
            required = true
    )
    private Path directory;

    public Path getDirectory() {
        return directory;
    }

    @Override
    public Integer call() throws Exception {

        return 0;
    }

}
