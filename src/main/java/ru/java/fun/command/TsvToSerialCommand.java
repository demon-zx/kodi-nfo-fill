package ru.java.fun.command;

import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.Logger;
import ru.java.fun.service.NfoService;
import ru.java.fun.service.TSVLoader;
import ru.java.fun.service.model.Movie;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@CommandLine.Command(
        name = "tsv-serial",
        aliases = {"csv", "tsv", "csv-serial"},
        mixinStandardHelpOptions = true,
        description = "Generate tv show info by tsv file"
)
public class TsvToSerialCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Serial name"
    )
    private String name;
    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    @Option(
            names = {"-ei", "--episodes-info"},
            description = "Episode list in csv format (tsv, tab separated): " +
                    "'Episode id', " +
                    "'Episode name', " +
                    "'Original episode name', " +
                    "'Premier date (date format)', " +
                    "'Description'"
    )
    private Path episodesInfoFile = Path.of("./episodes.csv");
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
        var seasons = TSVLoader.load(Files.newBufferedReader(episodesInfoFile, StandardCharsets.UTF_8));
        Movie serial = service.findSerial(file, name);
        if (serial == null) {
            if (StringUtils.isNotBlank(name)) {
                serial = service.fakeSerial(name);
            }
        }
        if (serial==null) {
            log().println(Logger.Level.INFO, "Need serial name");
        } else {
            service.fillSerial(file, extensions, serial, seasons, Set.of());
        }
        return 0;
    }

}
