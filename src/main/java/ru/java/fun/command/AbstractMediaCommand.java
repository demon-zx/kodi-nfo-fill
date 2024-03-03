package ru.java.fun.command;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import ru.java.fun.service.NfoService;

import java.nio.file.Path;
import java.util.Set;

@CommandLine.Command
public abstract class AbstractMediaCommand extends AbstractCommand {

    @SuppressWarnings("unused")
    @Option(
            names = {"-e", "--extensions"},
            description = "File extensions with movies split by comma (,)",
            defaultValue = "avi,mkv,mov,wmv,flv,webm,mpg,mpeg,mp2,mp3,mp4,m4v",
            split = ","
    )
    protected Set<String> extensions;

}
