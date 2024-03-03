package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;
import java.util.Set;

@CommandLine.Command(
        name = "fake-serial",
        aliases = "fsr",
        mixinStandardHelpOptions = true,
        description = "Generate tv show fake info"
)
public class FakeSerialCommand extends AbstractMediaCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-n", "--name"},
            description = "Serial name",
            required = true
    )
    private String name;
    @SuppressWarnings("unused")
    @Option(
            names = {"-epn", "--episode-prefix-name"},
            description = "Episode prefix name"
    )
    private String episodePrefixName = "Episode";
    @SuppressWarnings("unused")
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
        service.fakeSerial(getFile(), extensions, getName(), episodePrefixName);
        return 0;
    }

}
