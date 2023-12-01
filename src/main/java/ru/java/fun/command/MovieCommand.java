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

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        service.fill(getFile(), getName());
        return 0;
    }

}
