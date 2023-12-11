package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;

@CommandLine.Command(
        name = "serial",
        aliases = "sr",
        mixinStandardHelpOptions = true,
        description = "Scrap tv show info"
)
public class SerialCommand extends AbstractCommand {

    @Option(
            names = {"-n", "--name"},
            description = "Serial name"
    )
    private String name;

    @Option(
            names = {"-d", "--directory"},
            description = "Directory with serial",
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
        service.fillSerial(getFile(), getName());
        return 0;
    }

}
