package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;
import java.util.Set;

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
    @Option(
            names = {"-e", "--extensions"},
            description = "File extensions with movies split by comma (,)",
            defaultValue = "avi,mkv,mov,wmv,flv,webm,mpg,mpeg,mp2,mp3,mp4",
            split = ","
    )
    private Set<String> extensions;

    public String getName() {
        return name;
    }

    public Path getFile() {
        return file;
    }

    @Override
    public Integer call() throws Exception {
        NfoService service = new NfoService(log(), api());
        service.fillSerial(getFile(), extensions, getName());
        return 0;
    }

}
