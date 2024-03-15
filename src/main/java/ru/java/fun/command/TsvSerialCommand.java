package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;

@CommandLine.Command(
        name = "tsv-serial",
        aliases = {"csv", "tsv", "csv-serial"},
        mixinStandardHelpOptions = true,
        description = "Generate tv show info by tsv file"
)
public class TsvSerialCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Serial name"
    )
    private String name;
    @SuppressWarnings("unused")
    @Option(
            names = {"-ei", "--episodes-info"},
            description = "Episode list in csv (tsv, tab separated) format",
            required = true
    )
    private Path episodesInfoFile;
    @SuppressWarnings("unused")
    @Option(
            names = {"-d", "--directory"},
            description = "Directory with serial",
            required = true
    )
    private Path file;
    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        service.tsvSerial(file, extensions, name, episodesInfoFile);
        return 0;
    }

}
