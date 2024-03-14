package ru.java.fun.command;

import picocli.CommandLine;
import ru.java.fun.kinopoisk.dev.ApiClient;
import ru.java.fun.service.DataService;
import ru.java.fun.service.KPDDataService;
import ru.java.fun.service.Logger;
import ru.java.fun.service.PrintStreamLogger;
import ru.java.fun.util.Lazy;

import java.time.Duration;
import java.util.concurrent.Callable;

@CommandLine.Command
public abstract class AbstractCommand implements Callable<Integer> {

    @CommandLine.ParentCommand
    Root root;

    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "Verbose output"
    )
    protected boolean[] verbose = new boolean[0];

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    private final Lazy<Logger> log = new Lazy<>(() ->{
        CommandLine commandLine = spec.commandLine();
        Logger.Level level = Logger.Level.INFO;
        for (Logger.Level value : Logger.Level.values()) {
            if (value.value() == verbose.length) {
                level = value;
                break;
            }
        }
        return new PrintStreamLogger(commandLine.getOut(), level);
    });

    private final Lazy<ApiClient> api = new Lazy<>(() -> new ApiClient(
            root.getUri(),
            root.getToken(),
            log.get(),
            null,
            Duration.ofSeconds(15),
            Duration.ofSeconds(15)
    ));

    protected Logger log() {
        return log.get();
    }

    protected DataService api() {
        return new KPDDataService(log(), api.get());
    }
}
