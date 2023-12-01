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
import ru.java.fun.service.NfoFiles;
import ru.java.fun.service.NfoService;
import ru.java.fun.util.FileUtil;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Option(
            names = {"-e", "--extensions"},
            description = "File extensions with movies split by comma (,)",
            defaultValue = "avi,mkv,mov,wmv,flv,webm,mpg,mpeg,mp2,mp3,mp4",
            split = ","
    )
    private Set<String> extensions;
    @Option(
            names = {"-f", "--force"},
            description = "Force scan"
    )
    private boolean force;

    public Path getDirectory() {
        return directory;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        List<Path> files = Files.walk(getDirectory(), 1)
                .filter(p -> extensions.contains(FileUtil.extractExtension(p)))
                .collect(Collectors.toList());
        for (Path file : files) {
            String name = null;
            if (!force) {
                Path nfo = FileUtil.replaceExtension(file, ".nfo");
                if (Files.exists(nfo)) {
                    MovieNfo exists = NfoFiles.load(nfo);
                    name = exists.getOriginalTitle();
                }
            }
            service.fill(file, name);
        }
        return 0;
    }

}
