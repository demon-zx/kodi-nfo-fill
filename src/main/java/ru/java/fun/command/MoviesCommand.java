package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.service.NfoFiles;
import ru.java.fun.service.NfoService;
import ru.java.fun.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandLine.Command(
        name = "movies",
        aliases = "mvs",
        mixinStandardHelpOptions = true,
        description = "Scrap movies info in directory"
)
public class MoviesCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-d", "--directory"},
            description = "Directory with movies",
            required = true
    )
    private Path directory;

    @SuppressWarnings("unused")
    @Option(
            names = {"-u", "--update"},
            description = "Update exists, else scan only new files"
    )
    private boolean update;

    @SuppressWarnings("unused")
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
            Path nfoFile = FileUtil.replaceExtension(file, ".nfo");
            boolean exists = Files.exists(nfoFile);
            if (exists) {
                if (!force) {
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
            if(!exists || update) {
                service.fillMovie(file, name);
            }
        }
        return 0;
    }

}
