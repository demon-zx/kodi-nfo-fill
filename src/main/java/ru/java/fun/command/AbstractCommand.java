package ru.java.fun.command;

import picocli.CommandLine;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.service.Logger;
import ru.java.fun.service.PrintStreamLogger;
import ru.java.fun.util.Lazy;

import java.time.Duration;
import java.util.concurrent.Callable;

@CommandLine.Command
public abstract class AbstractCommand implements Callable<Integer> {

    @CommandLine.ParentCommand
    Root root;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    private final Lazy<Logger> log = new Lazy<>(() ->{
        CommandLine commandLine = spec.commandLine();
        return new PrintStreamLogger(commandLine.getOut());
    });

    private final Lazy<Api> api = new Lazy<>(() -> new Api(
            root.getUri(),
            root.getToken(),
            null,
            Duration.ofSeconds(15),
            Duration.ofSeconds(15)
    ));

    protected Logger log() {
        return log.get();
    }

    protected Api api() {
        return api.get();
    }
}
