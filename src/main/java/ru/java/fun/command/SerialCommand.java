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
public class SerialCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Serial name"
    )
    private String name;

    @SuppressWarnings("unused")
    @Option(
            names = {"-d", "--directory"},
            description = "Directory with serial",
            required = true
    )
    private Path file;

    @SuppressWarnings("unused")
    @Option(
            names = {"-s", "--special", "--with-special"},
            description = "Include special"
    )
    private boolean withSpecial;

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    public boolean isWithSpecial() {
        return withSpecial;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        service.fillSerial(getFile(), extensions, getName(), isWithSpecial());
        return 0;
    }

}
