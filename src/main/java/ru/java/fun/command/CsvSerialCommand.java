package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;

@CommandLine.Command(
        name = "csv-serial",
        aliases = "csv",
        mixinStandardHelpOptions = true,
        description = "Generate tv show fake info"
)
public class CsvSerialCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Serial name",
            required = true
    )
    private String name;
    @SuppressWarnings("unused")
    @Option(
            names = {"-el", "--episode-list"},
            description = "Episode list in csv (tsv, tab separated) format",
            required = true
    )
    private Path  episodesCsvFile;

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
//        service.fakeSerial(getFile(), extensions, getName(), episodePrefixName);
        return 0;
    }

}
