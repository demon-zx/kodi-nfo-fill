package ru.java.fun.command;

import org.apache.commons.text.StringEscapeUtils;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;
import ru.java.fun.service.TSVSaver;
import ru.java.fun.service.model.Episode;
import ru.java.fun.service.model.Season;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@CommandLine.Command(
        name = "serial-tsv",
        aliases = "sr-tsv",
        mixinStandardHelpOptions = true,
        description = "Scrap tv show info to tsv"
)
public class SerialToTsvCommand extends AbstractMediaCommand {

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

    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    @Option(
            names = {"-ei", "--episodes-info"},
            description = "Episode list in csv format (tsv, tab separated)"
    )
    private Path episodesInfoFile = Path.of("./episodes.csv");

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        var serial = service.findSerial(getFile(), getName());
        var seasons = api().findSeasonsById(serial.getId());
        try(PrintWriter writer = new PrintWriter(Files.newBufferedWriter(episodesInfoFile, StandardCharsets.UTF_8))) {
            TSVSaver.save(writer, seasons);
        }
        return 0;
    }

}
