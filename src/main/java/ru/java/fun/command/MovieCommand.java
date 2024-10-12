package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.service.Logger;
import ru.java.fun.service.NfoFiles;
import ru.java.fun.service.NfoService;
import ru.java.fun.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandLine.Command(
        name = "movie",
        aliases = "mv",
        mixinStandardHelpOptions = true,
        description = "Scrap movie info"
)
public class MovieCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Movie name"
    )
    private String name;

    @SuppressWarnings("unused")
    @Option(
            names = {"-f", "--file"},
            description = "File name",
            required = true
    )
    private Path file;

    @SuppressWarnings("unused")
    @Option(
            names = {"-u", "--update"},
            description = "Update exists, else scan only new files"
    )
    private boolean update;

    @SuppressWarnings("unused")
    @Option(
            names = {"--force"},
            description = "Force scan (not use old nfo file)"
    )
    private boolean force;

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        if (Files.isDirectory(file)) {
            movies(service, log(), file, update, force);
        } else {
            movie(service, log(), name, file, update, force);
        }
        return 0;
    }

    public void movies(NfoService service, Logger log, Path directory, boolean update, boolean force) throws Exception {
        List<Path> files;
        try (Stream<Path> stream = Files.walk(directory)) {
            files = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> extensions.contains(FileUtil.extractExtension(p)))
                    .collect(Collectors.toList());
        }
        for (Path file : files) {
            movie(service, log, null, file, update, force);
        }
    }

    public void movie(NfoService service, Logger log, String name, Path file, boolean update, boolean force) throws IOException {
        Path nfoFile = FileUtil.replaceExtension(file, ".nfo");
        boolean exists = Files.exists(nfoFile);
        if (exists) {
            if (!force) {
                log.printf(Logger.Level.INFO, "Use exists file %s%n", nfoFile);
                MovieNfo nfo = NfoFiles.load(nfoFile);
                name = Stream.of(
                                nfo.getOriginalTitle(),
                                nfo.getTitle()
                        )
                        .filter(Objects::nonNull)
                        .filter(s -> !s.isBlank())
                        .findFirst()
                        .orElse(null);
            }
        }
        if (!exists || update) {
            service.fillMovie(file, name);
        }
    }

}
