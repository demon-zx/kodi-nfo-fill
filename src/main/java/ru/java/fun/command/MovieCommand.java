package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;

@CommandLine.Command(
        name = "movie",
        aliases = "mv",
        mixinStandardHelpOptions = true,
        description = "Scrap movie info"
)
public class MovieCommand extends AbstractCommand {

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

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        service.fillMovie(getFile(), getName());
        return 0;
    }

}
